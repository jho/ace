/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.svm

import edu.berkeley.compbio.jlibsvm.util.SparseVector
import collection.JavaConversions._
import edu.berkeley.compbio.jlibsvm.ImmutableSvmParameterGrid
import edu.berkeley.compbio.jlibsvm.SolutionModel
import edu.berkeley.compbio.jlibsvm.binary.BinaryModel
import edu.berkeley.compbio.jlibsvm.binary.MutableBinaryClassificationProblemImpl
import edu.berkeley.compbio.jlibsvm.binary.Nu_SVC
import edu.berkeley.compbio.jlibsvm.kernel.LinearKernel
import org.jho.ace.util.Configuration
import org.jho.ace.util.LogHelper
import org.jho.ace.CipherText._

class GramSvm extends Configuration with LogHelper {
  val grams = language.trigramFrequencies.keys.toList.sorted
  logger.debug("Gram size: " + grams.size)
  var model:BinaryModel[String, SparseVector] = null

  def predict(text:String):Boolean = {
    val data = new SparseVector(grams.size())
    val freqs = text.trigramFrequencies
    grams.zipWithIndex.par.foreach { case(gram, i) => 
        data.indexes(i) = i; 
        freqs.get(gram) match {
          case Some(x) => data.values(i) = x.floatValue
          case None => 
        }
    }
    model.predictLabel(data).toBoolean
  }

  def score(text:String):Float = {
    val data = new SparseVector(grams.size())
    val freqs = text.trigramFrequencies
    grams.zipWithIndex.par.foreach { case(gram, i) => 
        data.indexes(i) = i; 
        freqs.get(gram) match {
          case Some(x) => data.values(i) = x.floatValue
          case None => 
        }
    }
    model.predictValue(data).asInstanceOf[Float]
  }

  def load:Boolean = {
    try {
      model = SolutionModel.identifyTypeAndLoad(getFilename).asInstanceOf[BinaryModel[String, SparseVector]]
      if(model == null) return false
      true
    } catch {
      case e => 
        false
    }
  }

  def train:Unit = {
    var problem = new MutableBinaryClassificationProblemImpl[String, SparseVector](classOf[String], grams.size)
    logger.debug("Generating positive samples...")
    (10 to 200).par.map(language.sample(_)).foreach { sample => 
      var data = new SparseVector(grams.size())
      var freqs = sample.trigramFrequencies
      grams.zipWithIndex.map { case(gram, i) => 
          data.indexes(i) = i; 
          freqs.get(gram) match {
            case Some(x) => data.values(i) = x.floatValue
            case None => 
          }
      }
      problem.addExample(data, "English")
    }

    logger.debug("Generating negative samples...")
    //now generate samples from random gibberish
    (10 to 200).par.map(language.randomString(_)).foreach { sample => 
      var data = new SparseVector(grams.size())
      var freqs = sample.trigramFrequencies
      grams.zipWithIndex.map { case(gram, i) => 
          data.indexes(i) = i; 
          freqs.get(gram) match {
            case Some(x) => data.values(i) = x.floatValue
            case None => 
          }
      }
      problem.addExample(data, "Garbage")
    }

    var builder = ImmutableSvmParameterGrid.builder[String, SparseVector]();
    builder.nu = 0.5f;
    builder.cache_size = 100;
    builder.eps = 1e-3f;
    builder.p = 0.1f;
    builder.shrinking = true;
    builder.probability = false;
    builder.redistributeUnbalancedC = true;
    builder.crossValidationFolds = 10;
    builder.Cset = Set(1.0f).asInstanceOf[Set[java.lang.Float]]
     
    var kernels = Set(new LinearKernel)
    builder.kernelSet = kernels
    val param = builder.build()
    var svm = new Nu_SVC[String, SparseVector]()

    logger.debug("Training svm model...")
    model = svm.train(problem, param)

    model.save(getFilename)

    logger.debug("Performing cross validation of model...")
    var cv = model.getCrossValidationResults();
    if (cv == null) {
      cv = svm.performCrossValidation(problem, param); 
    }
    println(cv.toString())
    logger.debug("Traning complete!")
  }

  protected def getFilename:String = {
    return List("svm_model", language.locale.getLanguage, language.locale.getCountry).mkString("_")
  }
}

object GramSvm extends Configuration {
  def main(args:Array[String]) = {
    val svm = new GramSvm
    println("Loading svm...")
    if(!svm.load) {
      println("Training new svm")
      svm.train
    } else {
      var avg = ((100 to 300).map(language.sample(_)).foldLeft(0.0) { (sum, e) => 
          val start = System.currentTimeMillis 
          println(svm.predict(e) == true)
          (System.currentTimeMillis - start)
        })/200
      println("Average prediction time: " + avg)

      avg = ((100 to 300).map(language.randomString(_)).foldLeft(0.0) { (sum, e) => 
          val start = System.currentTimeMillis 
          assert (svm.predict(e) == false)
          (System.currentTimeMillis - start)
        })/200
      println("Average prediction time: " + avg)
    }
  }
}
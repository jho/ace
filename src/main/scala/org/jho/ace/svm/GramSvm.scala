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
import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction
import edu.berkeley.compbio.jlibsvm.kernel.LinearKernel
import edu.berkeley.compbio.jlibsvm.scaler.ScalingModelLearner
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import org.jho.ace.util.Configuration
import org.jho.ace.util.LogHelper
import org.jho.ace.CipherText._

class GramSvm extends Configuration with LogHelper {
  val grams = language.trigramFrequencies.keys.toList.sorted
  var model:BinaryModel[String, SparseVector] = null

  def predict(text:String):String = {
    val data = new SparseVector(grams.size())
    grams.zipWithIndex.map { case(gram, i) => 
        data.indexes(i) = i; 
        if(text.trigramFrequencies.contains(gram)) {
          data.values(i) = text.trigramFrequencies(gram).asInstanceOf[Float]; 
        }
    }
    return model.predictLabel(data)
  }

  def score(text:String):Float = {
    val data = new SparseVector(grams.size())
    grams.zipWithIndex.map { case(gram, i) => 
        data.indexes(i) = i; 
        if(text.trigramFrequencies.contains(gram)) {
          data.values(i) = text.trigramFrequencies(gram).asInstanceOf[Float]; 
        }
    }
    return model.predictValue(data).asInstanceOf[Float]
  }


  def load:Boolean = {
    model = SolutionModel.identifyTypeAndLoad(getFilename).asInstanceOf[BinaryModel[String, SparseVector]]
    if(model == null) return false
    true
  }

  def train = {
    var samples = (10 to 500).map(language.sample(_))

    var problem = new MutableBinaryClassificationProblemImpl[String, SparseVector](classOf[String], grams.size)
    samples.foreach { sample => 
      var data = new SparseVector(grams.size())
      grams.zipWithIndex.map { case(gram, i) => 
          data.indexes(i) = i; 
          if(sample.trigramFrequencies.contains(gram)) {
            data.values(i) = sample.trigramFrequencies(gram).asInstanceOf[Float]; 
          }
      }
      problem.addExample(data, "English")
    }

    //now generate samples from random gibberish
    (10 to 500).map(language.randomString(_)).foreach { sample => 
      var data = new SparseVector(grams.size())
      grams.zipWithIndex.map { case(gram, i) => 
          data.indexes(i) = i; 
          if(sample.trigramFrequencies.contains(gram)) {
            data.values(i) = sample.trigramFrequencies(gram).asInstanceOf[Float]; 
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
    model = svm.train(problem, param)

    model.save(getFilename)

    var cv = model.getCrossValidationResults();
    if (cv == null) {
      cv = svm.performCrossValidation(problem, param); 
    }
    println(cv.toString())
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
      val start = System.currentTimeMillis 
      println(svm.predict("This is a test"));
      println(System.currentTimeMillis - start)
      println(svm.score("asdfqwer"));
      println(svm.score("This"));
      println(svm.score("This is a test"))
      println(svm.score(language.sample(50)))
      println(svm.score(language.sample(100)))
      println(svm.score(language.sample(150)))
      println(svm.score(language.sample(300)))
      println(svm.score(language.randomString(300)))
    }
  }
}
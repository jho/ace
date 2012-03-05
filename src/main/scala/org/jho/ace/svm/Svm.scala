/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.svm

import edu.berkeley.compbio.jlibsvm.util.SparseVector
import collection.JavaConversions._
import edu.berkeley.compbio.jlibsvm.ImmutableSvmParameterGrid
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

class Svm extends Configuration with LogHelper {
  var model:BinaryModel[String, SparseVector] = null

  def load:Boolean = {
    var is = getClass.getResourceAsStream("/"+getFilename)
    if(is == null) return false
    val in = new ObjectInputStream(is)
    model = in.readObject().asInstanceOf[BinaryModel[String, SparseVector]]
    if(model == null) return false
    true
  }

  def train = {
    var samples = (100 to 200).map(language.sample(_))
    val grams = language.trigramFrequencies.keys.toList.sorted

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
    (100 to 200).map(language.randomString(_)).foreach { sample => 
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

    val os = new FileOutputStream(new File(getFilename))
    val o = new ObjectOutputStream(os)
    o.writeObject(model)

    var cv = model.getCrossValidationResults();
    if (cv == null) {
      cv = svm.performCrossValidation(problem, param); 
    }
    println(cv.toString())

    //now generate samples from random gibberish
    (100 to 110).map(language.randomString(_)).foreach { sample => 
      var data = new SparseVector(grams.size())
      grams.zipWithIndex.map { case(gram, i) => 
          data.indexes(i) = i; 
          if(sample.trigramFrequencies.contains(gram)) {
            data.values(i) = sample.trigramFrequencies(gram).asInstanceOf[Float]; 
          }
      }
      model.predictLabel(data);
    }
  }

  protected def getFilename:String = {
    return List("svm_model_", language.locale.getLanguage, language.locale.getCountry).mkString("_")
  }
}

object Svm extends Configuration {
  def main(args:Array[String]) = {
    val svm = new Svm
    println("Loading svm...")
    if(!svm.load) {
      println("Training new svm")
      svm.train
    } 
  }
}
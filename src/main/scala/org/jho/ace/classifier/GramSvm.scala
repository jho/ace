/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.classifier

import edu.berkeley.compbio.jlibsvm.util.SparseVector
import collection.JavaConversions._
import edu.berkeley.compbio.jlibsvm.ImmutableSvmParameterGrid
import edu.berkeley.compbio.jlibsvm.SolutionModel
import edu.berkeley.compbio.jlibsvm.binary.BinaryModel
import edu.berkeley.compbio.jlibsvm.binary.C_SVC
import edu.berkeley.compbio.jlibsvm.binary.MutableBinaryClassificationProblemImpl
import edu.berkeley.compbio.jlibsvm.binary.Nu_SVC
import edu.berkeley.compbio.jlibsvm.kernel.GaussianRBFKernel
import edu.berkeley.compbio.jlibsvm.kernel.LinearKernel
import scala.math._
import org.jho.ace.util.Configuration
import org.jho.ace.util.LogHelper
import org.jho.ace.CipherText._
import scalala.library.Plotting
import scalala.tensor.mutable.Counter

class GramSvm extends LanguageClassifier with Configuration with LogHelper {
  val grams = language.trigramFrequencies.keys.toList.sorted.zipWithIndex
  var model:BinaryModel[java.lang.Boolean, SparseVector] = null

  def classify(text:String):Boolean = {
      model.predictLabel(vectorizeGrams(text)).booleanValue
  }

  def score(text:String):Float = {
    model.predictValue(vectorizeGrams(text)).asInstanceOf[Float]
  }

  def load:Boolean = {
    try {
      logger.debug("Loading svm...")
      model = SolutionModel.identifyTypeAndLoad(getFilename).asInstanceOf[BinaryModel[java.lang.Boolean, SparseVector]]
      if(model == null) return false
      logger.debug("Loading complete.")
      true
    } catch {
      case e => 
        false
    }
  }

  def train:Unit = {
    var problem = new MutableBinaryClassificationProblemImpl[java.lang.Boolean, SparseVector](classOf[Boolean], grams.size)
    logger.debug("Generating positive samples...")
    val range = (50 to 1000)
    range.map(language.sample(_)).foreach { sample => 
      //println("adding: "+sample)
      problem.addExample(vectorizeGrams(sample), true)
    }

    logger.debug("Generating negative samples...")
    //now generate samples from random gibberish
    range.map(language.randomString(_)).foreach { sample => 
      //println("adding: "+sample)
      problem.addExample(vectorizeGrams(sample), false)
    }

    var builder = ImmutableSvmParameterGrid.builder[java.lang.Boolean, SparseVector]()
    builder.nu = 0.5f;
    builder.cache_size = 100;
    builder.eps = 1e-3f;
    builder.p = 0.1f;
    builder.shrinking = true;
    builder.probability = false;
    builder.redistributeUnbalancedC = true;
    builder.crossValidationFolds = 10;
    //do grid search for C from 2^-5 to 2^15 
    //builder.Cset = (-5 to 5).map(pow(2, _).floatValue).toSet.asInstanceOf[Set[java.lang.Float]]
    builder.Cset = Set(1.0f).asInstanceOf[Set[java.lang.Float]]

    //do grid search for Gramm from 2^-15 to 2^3 
    //var kernels = (-15 to 3).map(pow(2,_)).map(gamma => new GaussianRBFKernel(gamma.floatValue))
    var kernels = Set(new LinearKernel)
    builder.kernelSet = kernels
    val param = builder.build()
    var svm = new Nu_SVC[java.lang.Boolean, SparseVector]()
    //var svm = new C_SVC[String, SparseVector]()

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

  protected def vectorizeGrams(text:String):SparseVector = {
    var data = new SparseVector(grams.size())
    var counter = Counter.count[String](text.trigrams).mapValues(_.toDouble)
    grams.foreach { case(gram, i) => 
      data.indexes(i) = i; 
      val value = counter(gram)
      if(value > 0.0) {
        data.values(i) = value.floatValue
      }
  }
  //println(data.values.mkString(","))
  return data
}

protected def getFilename:String = {
  return List("svm_model", language.locale.getLanguage, language.locale.getCountry).mkString("_")
}
}

object GramSvm extends Configuration {
  def main(args:Array[String]) = {
    val svm = new GramSvm
    println("Training svm...")
    svm.train
    println("Traning complete!")
  }
}
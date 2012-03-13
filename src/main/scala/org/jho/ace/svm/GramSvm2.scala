/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.svm

import collection.JavaConversions._
import scala.math._
import org.jho.ace.util.Configuration
import org.jho.ace.util.LogHelper
import org.jho.ace.CipherText._
import scalanlp.classify.SVM
import scalanlp.classify.SVM.Pegasos
import scalanlp.data.Example
import scalala.tensor.mutable.Counter

class GramSvm2 extends Configuration with LogHelper {
  val grams = language.trigramFrequencies.keys.toList.sorted.zipWithIndex

  def predict(text:String):Boolean = {
    false
  }

  def score(text:String):Float = {
    0.0f
  }

  def load:Boolean = {
    false
  }

  def train:Unit = {
    logger.debug("Generating positive samples...")
    val range = (10 to 100)
    var examples = range.map(language.sample(_)).map { sample => 
      Example("English", Counter.count[String](sample.trigrams).mapValues(_.toDouble))
    }

    logger.debug("Generating negative samples...")
    //now generate samples from random gibberish
    examples = examples ++: range.map(language.randomString(_)).map { sample => 
      Example("Gibberish", Counter.count[String](sample.trigrams).mapValues(_.toDouble))
    }

    val trainer = new SVM.Pegasos[String,Counter[String,Double]](200,batchSize=10)
    val testData = Array(
      Example("cat", Counter.count("claws","small").mapValues(_.toDouble))
    )
    logger.debug("Traning svm...")
    val model = trainer.train(examples)
    val r = model.classify(testData(0).features)
    println(r)

    logger.debug("Traning complete!")
  }

  /*
   protected def vectorizeGrams(text:String):SparseVector = {
   var data = new SparseVector(grams.size())
   var freqs = text.trigramFrequencies
   grams.map { case(gram, i) => 
   data.indexes(i) = i; 
   freqs.get(gram) match {
   case Some(x) => 
   //println(gram + "->" + i + "=" + x)
   data.values(i) = x.floatValue
   case None => 
   }
   }
   //println(data.values.mkString(","))
   return data
   }*/

  protected def getFilename:String = {
    return List("svm2_model", language.locale.getLanguage, language.locale.getCountry).mkString("_")
  }
}

object GramSvm2 extends Configuration {
  def main(args:Array[String]) = {
    val svm = new GramSvm2
    println("Training svm...")
    svm.train
    println("Traning complete!")
  }
}
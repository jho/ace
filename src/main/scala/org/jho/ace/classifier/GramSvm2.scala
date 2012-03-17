/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.classifier

import collection.JavaConversions._
import scala.math._
import org.jho.ace.util.Configuration
import org.jho.ace.util.LogHelper
import java.awt.Color
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import org.jho.ace.CipherText._
import scalala.tensor.sparse.SparseVector
import scalanlp.classify.SVM
import scalanlp.classify.SVM.Pegasos
import scalanlp.data.Example
import scalala.library.Plotting
import scalala.tensor.dense.DenseVector
import scalala.tensor.dense.DenseVectorCol
import scalala.tensor.mutable.Counter
import scalanlp.config._
import scalanlp.serialization.SerializationFormat._
import scalanlp.serialization.DataSerialization
import scalanlp.config._
import scalanlp.serialization.DataSerialization
import java.io._
import scalanlp.stats.ContingencyStats
import scalanlp.stats.random.HaltonSequence

class GramSvm2 extends LanguageClassifier with Configuration with LogHelper {
  val grams = language.trigramFrequencies.keys.toList.sorted.zipWithIndex
  val trainer = new SVM.Pegasos[Boolean,SparseVector[Double]](500, batchSize=500)
  var classifier:trainer.MyClassifier = null

  def classify(text:String):Boolean = {
    classifier.classify(vectorizeGrams(text))
  }

  def score(text:String):Float = {
    classifier.scores(vectorizeGrams(text)).max.floatValue
  }

  def load:Boolean = {
    logger.debug("Loading svm...")
    classifier = DataSerialization.read[trainer.MyClassifier](new ObjectInputStream(new FileInputStream(getFilename)))
    logger.debug("Loading complete.")
    return classifier != null
  }

  def train:Unit = {
    logger.debug("Generating positive samples...")
    val range = (50 to 1000)
    var positive = range.map(language.sample(_)).map { sample => 
      Example(true, vectorizeGrams(sample))
    }

    logger.debug("Generating negative samples...")
    //now generate samples from random gibberish
    val negative = range.map(language.randomString(_)).map { sample => 
      Example(false, vectorizeGrams(sample))
    }
    val examples = positive ++ negative

    logger.debug("Traning svm...")
    classifier = trainer.train(examples)

    val contingencyStats = ContingencyStats(classifier, examples)
    logger.info(contingencyStats);

    //visualize(examples)

    val out = new ObjectOutputStream(new FileOutputStream(getFilename))
    println(classifier.classify(vectorizeGrams(language.sample(100))))
    println(classifier.classify(vectorizeGrams(language.randomString(100))))
    DataSerialization.write(out, classifier)
    out.close()
    logger.debug("Traning complete.")
  }

  protected def vectorizeGrams(text:String):SparseVector[Double] = {
    var counter = Counter.count[String](text.trigrams).mapValues(_.toDouble)
    var vector = SparseVector.zeros[Double](grams.size)
    grams.foreach { case(gram, i) => 
        vector(i) = counter(gram)
    }
    return vector
  }

  protected def getFilename:String = {
    return List("svm2_model", language.locale.getLanguage, language.locale.getCountry).mkString("_")
  }

    /*
  protected def visualize(data: Iterable[Example[Boolean,SparseVector[Double]]]):Unit = {
    //require(data.head.features.size == 3)
    logger.debug("Features size: " + data.head.features.size)
    val xmin = data.map(_.features(1)).min
    val xmax = data.map(_.features(1)).max
    val ymin = data.map(_.features(2)).min
    val ymax = data.map(_.features(2)).max
    val points = for( v <- new HaltonSequence(data.head.features.size).sample(1000)) yield SparseVector(1.0, xmin + (xmax - xmin) * v.data(0), ymin + (ymax - ymin) * v.data(1))
    logger.debug("Points size: " + points.size)
    val x = new DenseVectorCol(points.map(_ apply 1).toArray)
    val y = new DenseVectorCol(points.map(_ apply 2).toArray)
    logger.debug(classifier.map{ case true => "+";  case false => "-"}(data.head.features))
    val classes = points.map(classifier.map{ case true => "+";  case false => "-"}).toIndexedSeq
    val colors = points.map(classifier.map{ case true => Color.BLUE;  case false => Color.WHITE}).toIndexedSeq
    val labelFunc : PartialFunction[Int,String] = { case x => classes(x)}

    val colorFunc : PartialFunction[Int,Color] = { case x => colors(x) }

    Plotting.scatter(x,y,DenseVector.ones[Double](x.size) * .002, colorFunc, labels = labelFunc)
  }*/

}

object GramSvm2 extends Configuration {
  def main(args:Array[String]) = {
    val svm = new GramSvm2
    svm.train
  }
}
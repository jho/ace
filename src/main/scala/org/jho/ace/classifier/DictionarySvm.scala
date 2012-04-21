/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.classifier

import org.jho.ace.util._

import collection.JavaConversions._
import scala.math._
import org.jho.ace.util.Configureable
import org.jho.ace.util.LogHelper
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import org.jho.ace.CipherText._
import scalala.tensor.sparse.SparseVector
import scalanlp.classify.SVM
import scalanlp.classify.SVM.Pegasos
import scalanlp.data.Example
import scalala.tensor.mutable.Counter
import scalanlp.config._
import scalanlp.serialization.SerializationFormat._
import scalanlp.serialization.DataSerialization
import scalanlp.config._
import scalanlp.serialization.DataSerialization
import java.io._
import scalanlp.stats.ContingencyStats
import scala.util.Random

class DictionarySvm extends DecryptionClassifier with Configureable with LogHelper {
  private val minSize = 3 
  private val words = language.wordFrequencies.toList
    .map(_._1)
    .filter(_.size >= minSize)
    .sorted
    .zipWithIndex

  private val trainer = new SVM.Pegasos[Boolean,SparseVector[Double]](500, 0.5, 600)
  private var classifier:trainer.MyClassifier = null

  def classify(text:String):Boolean = {
    var score = classifier.scores(vectorize(text))
    if(!score.pairsIterator.hasNext)  //if there was no match at all assume false
      return false
    else 
      return score.argmax
  }

  def score(text:String):Float = {
    var score = classifier.scores(vectorize(text))
    if(!score.pairsIterator.hasNext)  //if there was no match at all assume false
      return -1000
    else if(score.argmax == false) 
      return -(score.max.floatValue)
    return score.max.floatValue
  }

  def load:Boolean = {
    logger.debug("Loading svm...")
    classifier = DataSerialization.read[trainer.MyClassifier](new ObjectInputStream(new FileInputStream(getFilename)))
    logger.debug("Loading complete.")
    return classifier != null
  }

  def train:Unit = {
    logger.debug("Generating positive samples...")
    val zeros = SparseVector.zeros[Double](words.size)
    val range = (1 to 2) flatMap (_ => (50 to 2000).toList)
    val positive = range.map(i => vectorize(language.sample(i))).withFilter(_ != zeros).map(Example(true, _))

    logger.debug("Generating negative samples...")
    //now generate some partial examples of strings that are nearly correct
    //but have some slight mutations.  This mimics partially correct decryptions
    //which can have very similar statistics to correct decryptions
    val negative = range.map(i => vectorize(DecryptionClassifier.mimicPartialDecryption(language.sample(i)))).withFilter(_ != zeros).map(Example(false, _))
    logger.debug("Negative examples: "+negative.size)
    val examples = positive ++ negative

    logger.debug("Traning svm...")
    classifier = trainer.train(examples)

    val contingencyStats = ContingencyStats(classifier, examples)
    logger.info(contingencyStats);

    val out = new ObjectOutputStream(new FileOutputStream(getFilename))
    println(classify(language.sample(100)))
    println(classify(language.randomString(100)))
    DataSerialization.write(out, classifier)
    out.close()
    logger.debug("Traning complete.")
  }

  protected def vectorize(text:String):SparseVector[Double] = {
    var counter = Counter.count[String](text.words.filter(_.size >= minSize)).mapValues(_.toDouble)
    var vector = SparseVector.zeros[Double](words.size)
    words.foreach { case(word, i) => 
        vector(i) = counter(word)
    }
    return vector
  }

  protected def getFilename:String = {
    return List("svm2_model", language.locale.getLanguage, language.locale.getCountry).mkString("_")
  }
}

object DictionarySvm extends Configureable {
  def main(args:Array[String]) = {
    val svm = new DictionarySvm
    //println(svm.mimicPartialDecryption("FOOBARFOOBARFOOBAR"))
    svm.train
  }
}

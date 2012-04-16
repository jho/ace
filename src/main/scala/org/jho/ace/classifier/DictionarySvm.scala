/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.classifier

import org.jho.ace.util._

import collection.JavaConversions._
import scala.math._
import org.jho.ace.util.Configuration
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

class GramSvm2 extends LanguageClassifier with Configuration with LogHelper {
    val grams = language.trigramFrequencies.keys.toList.sorted.zipWithIndex

    val trainer = new SVM.Pegasos[Boolean,SparseVector[Double]](500, batchSize=200)
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
        //val range = (50 to 52)
        var positive = range.map(language.sample(_)).map { sample => 
            Example(true, vectorizeGrams(sample))
        }

        logger.debug("Generating negative samples...")
        /*
        //now generate samples from random gibberish
        var negative = range.filter(_ % 2 == 0).map(language.randomString(_)).map { sample => 
            Example(false, vectorizeGrams(sample))
        }*/

        //now generate some partial examples of strings that are nearly correct
        //but have some slight mutations.  This mimics partially correct decryptions
        //which can have very similar statistics to correct decryptions
        val rand = new Random();
        val negative = range.map(language.sample(_)).map { sample => 
          println(sample)
          var cols = rand.nextInt(sample.size-1)
          if(cols == 0) cols = 1
          val indices = (0 to cols).map{ col => 
            val pos = rand.nextInt(sample.size-1) 
            (pos to sample.size - 1 by pos)
          }
          val mutated = sample.zipWithIndex.map {case(c, i) => 
            if (indices.contains(i)) language.randomChar 
            else c
          }.mkString
          Example(false, vectorizeGrams(mutated))
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
}

object GramSvm2 extends Configuration {
    def main(args:Array[String]) = {
        val svm = new GramSvm2
        svm.train
    }
}

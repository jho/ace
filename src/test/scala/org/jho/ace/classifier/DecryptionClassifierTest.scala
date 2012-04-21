/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.classifier

import org.jho.ace.util.Configureable
import org.jho.ace.util.LogHelper
import org.jho.ace.util._

import org.junit._
import Assert._

import scala.util.Random

abstract class LanguageClassifierTest(val classifier:DecryptionClassifier) extends Configureable with LogHelper {
  classifier.load
  val range = (1 to 2) flatMap (_ => (50 to 550).toList)

  @Test
  def testPositive = {
    var errors = 0
    var avg = range.map(language.sample(_)).foldLeft(0.0) { (sum, e) => 
      val start = System.currentTimeMillis 
      val prediction = classifier.classify(e)
      if(!prediction) {
        println("Text Length: " + e.length)
        println("Prediction: " + prediction)
        println("Score: " + classifier.score(e))
        errors += 1
      }
      sum + (System.currentTimeMillis - start)
    }/range.size
    println("Average prediction time: " + avg)

    var errorRate = errors/range.size.toDouble
    println("Error rate: " + errorRate)
    assertTrue(errorRate <= .05)

    assertTrue(classifier.classify(language.sample(1000)))
    assertTrue(classifier.classify(language.sample(10000)))
  }

  @Test
  def testNegative = {
    var errors = 0
    val rand = new Random()
    var samples = range.map{ i =>  
      var sample = language.sample(i)
      (sample, DecryptionClassifier.mimicPartialDecryption(sample))
    }
    var avg = samples.foldLeft(0.0) { (sum, e) => 
      val start = System.currentTimeMillis 
      val prediction = classifier.classify(e._2)
      if(prediction) {
        println("-----")
        //println("Text: " + e._2)
        //println("Original: " + e._1)
        println("Text Length: " + e._2.length)
        println("Diff from plaintext: " + e._1.distance(e._2))
        println("Prediction: " + prediction)
        println("Score: " + classifier.score(e._2))
        errors += 1
      }
      sum + (System.currentTimeMillis - start)
    }/range.size
    println("Average prediction time: " + avg)
    var errorRate = errors/range.size.toDouble
    println("Error rate: " + errorRate)
    assertTrue(errorRate <= .05)
    
    assertFalse(classifier.classify(language.randomString(1000)))
    assertFalse(classifier.classify(language.randomString(10000)))
  }

}

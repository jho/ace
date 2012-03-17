/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.classifier

import org.jho.ace.util.Configuration
import org.jho.ace.util.LogHelper
import org.junit._
import Assert._

abstract class LanguageClassifierTest(val classifier:LanguageClassifier) extends Configuration with LogHelper {
  classifier.load
  val range = (50 to 100)

  @Test
  def testPositive = {
    var errors = 0
    var avg = range.map(language.sample(_)).foldLeft(0.0) { (sum, e) => 
      val start = System.currentTimeMillis 
      //logger.debug("Predicting for: " + e)
      val prediction = classifier.classify(e)
      //if(!prediction) {
        println("Text: " + e)
        println("Text Length: " + e.length)
        println("Prediction: " + prediction)
        println("Score: " + classifier.score(e))
      if(!prediction) {
        errors += 1
      }
      sum + (System.currentTimeMillis - start)
    }/range.size
    println("Average prediction time: " + avg)

    assertEquals(0, errors)

    assertTrue(classifier.classify(language.sample(1000)))
    assertTrue(classifier.classify(language.sample(10000)))
  }

  @Test
  def testNegative = {
    var errors = 0
    val avg = range.map(language.randomString(_)).foldLeft(0.0) { (sum, e) => 
      val start = System.currentTimeMillis 
      val prediction = classifier.classify(e)
      val score = classifier.score(e)
      //if(prediction) {
        println("Text: " + e)
        println("Text Length: " + e.length)
        println("Prediction: " + prediction)
        println("Score: " + classifier.score(e))
      if(prediction) {
        errors += 1
      }
      sum + (System.currentTimeMillis - start)
    }/range.size
    println("Average prediction time: " + avg)

    assertEquals(0, errors)
    
    assertFalse(classifier.classify(language.randomString(1000)))
    assertFalse(classifier.classify(language.randomString(10000)))
  }

}

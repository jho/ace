/*
 * Copyright 2012 Photobucket 
 */

package com.jho.ace.svm

import org.jho.ace.svm.GramSvm
import org.jho.ace.util.Configuration
import org.jho.ace.util.LogHelper
import org.junit._
import Assert._
import scala.collection.mutable.ListBuffer

class GramSvmTest extends Configuration with LogHelper {
  val svm = new GramSvm   
  svm.load
  val range = (10 to 300)

  @Test
  def testPositive = {
    var errors = 0
    var avg = range.map(language.sample(_)).foldLeft(0.0) { (sum, e) => 
      val start = System.currentTimeMillis 
      //logger.debug("Predicting for: " + e)
      val prediction = svm.predict(e)
      if(!prediction) {
        println("Text: " + e)
        println("Prediction: " + prediction)
        println("Score: " + svm.score(e))
        errors += 1
      }
      sum + (System.currentTimeMillis - start)
    }/range.size
    println("Average prediction time: " + avg)

    assertEquals(0, errors)

    assertTrue(svm.predict(language.sample(1000)))
    assertTrue(svm.predict(language.sample(10000)))
  }

  @Test
  def testNegative = {
    var errors = 0
    val avg = range.map(language.randomString(_)).foldLeft(0.0) { (sum, e) => 
      val start = System.currentTimeMillis 
      val prediction = svm.predict(e)
      val score = svm.score(e)
      if(prediction) {
        println("Text: " + e)
        println("Prediction: " + prediction)
        println("Score: " + svm.score(e))
        errors += 1
      }
      sum + (System.currentTimeMillis - start)
    }/range.size
    println("Average prediction time: " + avg)

    assertEquals(0, errors)
    
    assertFalse(svm.predict(language.randomString(1000)))
    assertFalse(svm.predict(language.randomString(10000)))
  }

}

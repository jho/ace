/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import org.jho.ace.CipherText._
import org.jho.ace.util.Configuration

class CipherTextTest extends Configuration {
  @Test
  def testFrequencies = {
    var results = "This is a test".frequencies
    assertEquals(6, results.size)
    results.foreach { p => assertTrue(p._2 > 0.0) }
  }

  @Test
  def testBigramFrequencies = {
    var results = "This is a test".bigramFrequencies
    assertEquals(9, results.size)
    results.foreach { p => assertTrue(p._2 > 0.0) }
  }

  @Test
  def testNgramFrequencies = {
    var results = "This is a test".ngramFrequencies(3)
    assertEquals(9, results.size)
    results.foreach { p => assertTrue(p._2 > 0.0) }
  }

  @Test
  def findKeyLength = {
    var result = "QPWKALVRXCQZIKGRBPFAEOMFLJMSDZVDHXCXJYEBIMTRQWNMEAIZRVKCVKVLXNEICFZPZCZZHKMLVZVZIZRRQWDKECHOSNYXXLSPMYKVQXJTDCIOMEEXDQVSRXLRLKZHOV".findKeyLength
    assertEquals(5, result)
  }
}

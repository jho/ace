/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.IndexOfCoincidence._
import org.jho.ace.util.Configuration

import org.junit._
import Assert._

class IndexOfCoincidenceTest extends Configuration {
  @Test
  def testComputeIoC = {
    var result = "This is a really long string that should have an index of coincidence similar to that of general written English".computeIoC
    println(result)
    assertTrue(result >= 1.64)
    assertTrue(result <= 1.65)
  }

  @Test
  def testPeriod = {
    var result = "QPWKALVRXCQZIKGRBPFAEOMFLJMSDZVDHXCXJYEBIMTRQWNMEAIZRVKCVKVLXNEICFZPZCZZHKMLVZVZIZRRQWDKECHOSNYXXLSPMYKVQXJTDCIOMEEXDQVSRXLRLKZHOV".testPeriod
    /*
    result = "LXFOPVEFRNHR".testPeriod
    println(result.toList.sortBy(_._1))
    */
  }

  @Test
  def findKeyLength = {
    var result = "QPWKALVRXCQZIKGRBPFAEOMFLJMSDZVDHXCXJYEBIMTRQWNMEAIZRVKCVKVLXNEICFZPZCZZHKMLVZVZIZRRQWDKECHOSNYXXLSPMYKVQXJTDCIOMEEXDQVSRXLRLKZHOV".findKeyLength
    println(result)
    assertEquals(5, result)
  }
}

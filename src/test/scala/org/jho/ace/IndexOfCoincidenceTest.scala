/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.IndexOfCoincidence._

import org.junit._
import Assert._

class IndexOfCoincidenceTest {
  @Test
  def testComputeIoC = {
    var result = "This is a really long string that should have an index of coincidence similar to that of general written English".computeIoC
    println(result)
    //assertTrue(0.943076923076923, result)
  }

  @Test
  def testPeriod = {
    println("QPWKALVRXCQZIKGRBPFAEOMFLJMSDZVDHXCXJYEBIMTRQWNMEAIZRVKCVKVLXNEICFZPZCZZHKMLVZVZIZRRQWDKECHOSNYXXLSPMYKVQXJTDCIOMEEXDQVSRXLRLKZHOV".testPeriod.toList.sortBy(_._1))
    var result = "LXFOPVEFRNHR".testPeriod
    println(result.toList.sortBy(_._1))
  }

  //@Test
  def findKeyLength = {
    var result = "QPWKALVRXCQZIKGRBPFAEOMFLJMSDZVDHXCXJYEBIMTRQWNMEAIZRVKCVKVLXNEICFZPZCZZHKMLVZVZIZRRQWDKECHOSNYXXLSPMYKVQXJTDCIOMEEXDQVSRXLRLKZHOV".findKeyLength
    println(result)
  }
}

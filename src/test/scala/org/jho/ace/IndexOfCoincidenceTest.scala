/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.IndexOfCoincidence._

import org.junit._
import Assert._

class IndexOfCoincidenceTest {
  //@Test
  def testComputeIoC = {
    var result = "This is a really long string that should have an index of coincidence similar to that of general written English".computeIoC
    assertTrue(1.640953716690042 == result)
  }

  @Test
  def testPeriod = {
    var result = "QPWKALVRXCQZIKGRBPFAEOMFLJMSDZVDHXCXJYEBIMTRQWNMEAIZRVKCVKVLXNEICFZPZCZZHKMLVZVZIZRRQWDKECHOSNYXXLSPMYKVQXJTDCIOMEEXDQVSRXLRLKZHOV".testPeriod
    println(result.toList.sortBy(_._1))
    /*
    result = "LXFOPVEFRNHR".testPeriod
    println(result.toList.sortBy(_._1))
    */
  }

  @Test
  def findKeyLength = {
    var result = "QPWKALVRXCQZIKGRBPFAEOMFLJMSDZVDHXCXJYEBIMTRQWNMEAIZRVKCVKVLXNEICFZPZCZZHKMLVZVZIZRRQWDKECHOSNYXXLSPMYKVQXJTDCIOMEEXDQVSRXLRLKZHOV".findKeyLength
    println(result)
  }
}

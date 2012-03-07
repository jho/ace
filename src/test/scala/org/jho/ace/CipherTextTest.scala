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
    val results = "This is a test".frequencies
    assertEquals(6, results.size)
    results.foreach { p => assertTrue(p._2 > 0.0) }
  }

  @Test
  def testBigramFrequencies = {
    val results = "This is a test".bigramFrequencies
    assertEquals(9, results.size)
    results.foreach { p => assertTrue(p._2 > 0.0) }
  }

  @Test
  def testNgramFrequencies = {
    val results = "This is a test".ngramFrequencies(3)
    assertEquals(9, results.size)
    results.foreach { p => assertTrue(p._2 > 0.0) }
  }

  @Test
  def testNgrams {
    val start = System.currentTimeMillis
    val results = language.sample(500).ngramFrequencies(4)
    println(System.currentTimeMillis - start)
  }

  @Test
  def findKeyLength = {
    var results = "QPWKALVRXCQZIKGRBPFAEOMFLJMSDZVDHXCXJYEBIMTRQWNMEAIZRVKCVKVLXNEICFZPZCZZHKMLVZVZIZRRQWDKECHOSNYXXLSPMYKVQXJTDCIOMEEXDQVSRXLRLKZHOV".keyLengths
    assertEquals(5, results(0))
    assertEquals(15, results(1))
    assertEquals(20, results(2))

    /*
     println("CIABIRCEPOQYNIRYFCPSSXHMEXUSXFDMKO".periods)
     println("CIABIRCEPOQYNIRYFCPSSXHMEXUSXFDMKO".keyLengths)

     var cipherText = "DLCNSRUORMOSTUOXQPVRWDMKARFHCRMPFVYOEJ"
     println(cipherText.periods.toList.sortWith(_._1 < _._1))
     println(cipherText.keyLengths)
     println(cipherText.columnFrequencies(cipherText.keyLengths.head))*/
  }
}

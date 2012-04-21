/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import org.jho.ace.CipherText._
import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util._

class CipherTextTest extends Configureable {
  @Test
  def testFrequencies = {
    val results = "This is a test".frequencies
    assertEquals(6, results.size)
    results.foreach { p => assertTrue(p._2 > 0.0) }
  }

  @Test
  def testBigramFrequencies = {
    val results = "This is a test".bigramFrequencies
    assertEquals(5, results.size)
    results.foreach { p => assertTrue(p._2 > 0.0) }
  }

  @Test
  def testNgramFrequencies = {
    val results = "This is a test".ngramFrequencies(3)
    assertEquals(5, results.size)
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
    val cipher = new Vigenere()
    var results = 100.times {
        cipher.encrypt("KEYWORD", language.sample(200)).keyLengths
    }
    val accuracy = (results.filter(_.take(3).contains(7)).size*1.0/results.size)
    println(accuracy)
    assertTrue(accuracy > .90)

    /*
     println("CIABIRCEPOQYNIRYFCPSSXHMEXUSXFDMKO".periods)
     println("CIABIRCEPOQYNIRYFCPSSXHMEXUSXFDMKO".keyLengths)

     var cipherText = "DLCNSRUORMOSTUOXQPVRWDMKARFHCRMPFVYOEJ"
     println(cipherText.periods.toList.sortWith(_._1 < _._1))
     println(cipherText.keyLengths)
     println(cipherText.columnFrequencies(cipherText.keyLengths.head))*/
  }
}

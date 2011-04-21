/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Configuration

abstract class CryptanalyzerTestBase(val algorithm:Cryptanalyzer) extends Configuration {
  @Test
  def decryptLongText = {
    var result = algorithm.decrypt("QPWKALVRXCQZIKGRBPFAEOMFLJMSDZVDHXCXJYEBIMTRQWNMEAIZRVKCVKVLXNEICFZPZCZZHKMLVZVZIZRRQWDKECHOSNYXXLSPMYKVQXJTDCIOMEEXDQVSRXLRLKZHOV")
    println(result)
    assertEquals("MUSTCHANGEMEETINGLOCATIONFROMBRIDGETOUNDERPASSSINCEENEMYAGENTSAREBELIEVEDTOHAVEBEENASSIGNEDTOWATCHBRIDGESTOPMEETINGTIMEUNCHANGEDXX", result)
    println("--------------")
  }

  @Test
  def decryptRandomSample = {
    var sample = language.sample(100)
    println(sample)
    var encrypted = new Vigenere("KEY").encrypt(sample)
    println(encrypted)
    var result = algorithm.decrypt(encrypted)
    println(result)
    assertEquals(result, encrypted)
    println("--------------")
  }

  @Test
  def decryptShortText = {
    /*
     var cipherText = "LXFOPVEFRNHR"
     println(heuristics.foldLeft(0.0)(_ + _.evaluate(cipherText)))
     var result = algorithm.decrypt(cipherText)
     println(result)
     assertEquals("ATTACKATDAWN", result)
     println("--------------")
     */
  }
}

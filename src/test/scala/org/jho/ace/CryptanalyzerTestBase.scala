/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import org.jho.ace.ciphers.Cipher
import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Configuration
import org.jho.ace.util.Util._

abstract class CryptanalyzerTestBase(val algorithm:Cryptanalyzer) extends Configuration {
  /*
  @Test
  def decryptLongText = {
    testDecrypt("MUSTCHANGEMEETINGLOCATIONFROMBRIDGETOUNDERPASSSINCEENEMYAGENTSAREBELIEVEDTOHAVEBEENASSIGNEDTOWATCHBRIDGESTOPMEETINGTIMEUNCHANGEDXX",
                "EVERY", new Vigenere)
  }*/

  @Test
  def decryptLongRandomSample = {
    var plainText = language.sample(150)
    testDecrypt(plainText, "LEMON", new Vigenere)
  }

  @Test
  def decryptMediumRandomSample = {
    var plainText = language.sample(100)
    testDecrypt(plainText, "LEMON", new Vigenere)
  }

  @Test
  def decryptShortRandomSample = {
    var plainText = language.sample(75)
    testDecrypt(plainText, "LEMON", new Vigenere)
  }

  /*
  @Test
  def decryptShortText = {
    testDecrypt("ATTACKATDAWN", "LEMON", new Vigenere)
  }*/

  private def testDecrypt(plainText:String, key:String, cipher:Cipher) = {
    println("Plain Text: " + plainText)
    println("Plain Text Cost: " + heuristics.foldLeft(0.0)(_ + _.evaluate(plainText)))
    var cipherText = cipher.encrypt(key, plainText)
    println("Cipher Text: " + cipherText)
    var result = algorithm.decrypt(cipherText, cipher)
    println(result)
    println("Resulting decryption: " + result)
    var diff = result.plainText.diff(plainText)
    println("Diff from original plain text: " + diff)
    //assertTrue("Difference is greater than 30%", (diff <= .30))
    println("--------------")
  }

}

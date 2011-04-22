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
  @Test
  def decryptLongText = {
    testDecrypt("MUSTCHANGEMEETINGLOCATIONFROMBRIDGETOUNDERPASSSINCEENEMYAGENTSAREBELIEVEDTOHAVEBEENASSIGNEDTOWATCHBRIDGESTOPMEETINGTIMEUNCHANGEDXX",
                "EVERY", new Vigenere)
  }

  @Test
  def decryptRandomSample = {
    var plainText = language.sample(100)
    testDecrypt(plainText, "KEY", new Vigenere)
  }

  @Test
  def decryptShortText = {
    println(heuristics.foldLeft(0.0)(_ + _.evaluate("ATTACKATDAWN")))
    testDecrypt("ATTACKATDAWN", "LEMON", new Vigenere)
  }

  private def testDecrypt(plainText:String, key:String, cipher:Cipher) = {
    println("Plain Text: " + plainText)
    var cipherText = cipher.encrypt(key, plainText)
    println("Cipher Text: " + cipherText)
    var result = algorithm.decrypt(cipherText, cipher)
    println("Resulting decryption: " + result)
    var diff = result.diff(plainText)
    println("Diff from original plain text: " + diff)
    assertTrue("Difference is greater than 20%", (diff <= .20))
    println("--------------")
  }

}

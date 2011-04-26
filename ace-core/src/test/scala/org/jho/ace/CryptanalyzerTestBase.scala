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
  def decryptRandomSamples = {
    for ( i <- (50 to 200 by 25)) {
      println(i)
      testDecrypt(language.sample(i), "LEMON", new Vigenere)
    }
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
      var startTime = System.currentTimeMillis
      var result = algorithm.decrypt(cipherText, cipher)
      println("Time: " + (System.currentTimeMillis-startTime))
      println(result)
      println("Resulting decryption: " + result)
      var diff = result.plainText.diff(plainText)
      println("Diff from original plain text: " + diff)
      //assertTrue("Difference is greater than 30%", (diff <= .30))
      println("--------------")
    }

   }

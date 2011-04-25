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

class CryptanalyzerPerfTest extends Configuration {
  @Test
  def performanceTest = {
    10.times {
      var plainText = language.sample(150)
      testDecrypt(plainText, "LEMON", new Vigenere)
    }
  }

  private def testDecrypt(plainText:String, key:String, cipher:Cipher) = {
    //println("Plain Text: " + plainText)
    var cipherText = cipher.encrypt(key, plainText)
    //println("Cipher Text: " + cipherText)

    var astar = new AStarCryptanalyzer
    var sa = new SACryptanalyzer

    println("------A* Run-----")
    var astarResult = astar.decrypt(cipherText, cipher)
    println("------SA Run-----")
    var saResult = sa.decrypt(cipherText, cipher)
    var astarDiff = astarResult.diff(plainText)
    var saDiff = saResult.diff(plainText)
    println("A* accuracy: " + (1.0 - astarDiff))
    println("SA accuracy: " + (1.0 - saDiff))
    println("--------------------")
  }

}

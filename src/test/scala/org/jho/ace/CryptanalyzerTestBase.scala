/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import org.jho.ace.ciphers.Cipher
import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Configureable
import org.jho.ace.util._

abstract class CryptanalyzerTestBase(val algorithm:Cryptanalyzer) extends Configureable with LogHelper {
  //@Test
  def decryptLongText = {
    testDecrypt("MUSTCHANGEMEETINGLOCATIONFROMBRIDGETOUNDERPASSSINCEENEMYAGENTSAREBELIEVEDTOHAVEBEENASSIGNEDTOWATCHBRIDGESTOPMEETINGTIMEUNCHANGEDXX",
                "EVERY", new Vigenere)
  }

  //@Test
  def decryptShortText = {
    testDecrypt("THEREARENOSECRETSTHATTIMEDOESNOTREVEAL",
                "KEYWORD", new Vigenere)
  }

  //@Test
  def decryptRandomSamplesShortKeyword = {
    for ( i <- (100 to 200 by 50)) {
      var keyword = language.dictionary.randomWord(3)
      logger.debug("Keyword: " + keyword + ", Size: " + i)
      testDecrypt(language.sample(i), keyword, new Vigenere)
    }
  }

  @Test
  def decryptRandomSamplesMediumKeyword = {
    for ( i <- (100 to 200 by 50)) {
      var keyword = language.dictionary.randomWord(7)
      logger.debug("Keyword: " + keyword + ", Size: " + i)
      testDecrypt(language.sample(i), keyword, new Vigenere)
    }
  }

  //@Test
  def decryptRandomSamplesLongKeyword = {
    for ( i <- (100 to 200 by 50)) {
      var keyword = language.dictionary.randomWord(10)
      logger.debug("Keyword: " + keyword + ", Size: " + i)
      testDecrypt(language.sample(i), keyword, new Vigenere)
    }
  }

  private def testDecrypt(plainText:String, key:String, cipher:Cipher) = {
    var cipherText = cipher.encrypt(key, plainText)
    var startTime = System.currentTimeMillis
    var result = algorithm.decrypt(cipherText, cipher)
    logger.debug("Time: " + (System.currentTimeMillis-startTime))
    logger.debug("Resulting decryption: " + result)
    var diff = result.plainText.distance(plainText)
    logger.debug("Diff from original plain text: " + diff)
    //assertTrue("Difference is greater than 50%", (diff <= .50))
    logger.debug("--------------")
  }

}

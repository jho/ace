/*
 * Copyright 2011 Joshua Hollander.
 */

package org.jho.ace.ciphers

import org.jho.ace.util.Util._
import org.jho.ace.CipherText._
import org.jho.ace.util.Language

class Vigenere extends Cipher {
  def encrypt(key:String, message:String):String = {
    message.view.filter(_.isLetter).map(_.toUpper).zipWithIndex.foldLeft("") {
      case(cipherText,(value,index)) => {
          cipherText + language.int2Char((language.char2Int(value) + language.char2Int(key(index % key.length))) mod 26)
        }
    }
  }

  def decrypt(key:String, cipherText:String):String = {
    cipherText.view.filter(_.isLetter).map(_.toUpper).zipWithIndex.foldLeft("") {
      case(message,(value,index)) => {
          message + language.int2Char((language.char2Int(value) - language.char2Int(key(index % key.length))) mod 26)
        }
    }
  }

  /**
   * "Guess" at an initial using the IoC method, and choosing the highest frequency in the language for each key
   */
  def generateInitialKey(cipherText:String):String = {
    var keyLength = cipherText.keyLengths.head
    var colFreqs = cipherText.columnFrequencies(keyLength)
    colFreqs.foldLeft("")( _ + _._2.head._1 )
  }
}
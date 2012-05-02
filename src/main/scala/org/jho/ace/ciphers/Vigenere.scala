/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.ciphers

import org.jho.ace.Key
import org.jho.ace.Key._
import org.jho.ace.Keyword
import org.jho.ace.StringCipherText
import org.jho.ace.heuristic.Heuristic
import org.jho.ace.util._
import org.jho.ace.CipherText
import org.jho.ace.CipherText._
import scala.math._

class Vigenere extends Cipher[Keyword, StringCipherText] with LogHelper {
  def encrypt(key:Keyword, message:StringCipherText):StringCipherText = {
    message.text.view.filter(_.isLetter).map(_.toUpper).zipWithIndex.foldLeft("") {
      case(cipherText,(value,index)) => {
          cipherText + language.int2Char((language.char2Int(value) + language.char2Int(key.text(index % key.length))) mod 26)
        }
    }
  }

  def decrypt(key:Keyword, cipherText:StringCipherText):StringCipherText = {
    cipherText.text.view.filter(_.isLetter).map(_.toUpper).zipWithIndex.foldLeft("") {
      case(message,(value,index)) => {
          message + language.int2Char((language.char2Int(value) - language.char2Int(key.text(index % key.length))) mod 26)
        }
    }
  }

  /**
   * "Guess" at an initial using the IoC method, and choosing the highest frequency in the language for each key
   */
  def generateInitialKey(cipherText:StringCipherText, goal:Double = 0.0)(implicit heuristic:Heuristic):Keyword = {
    //return language.frequencies.head._1.toString
    var c = language.frequencies.head._1.toString
    var initialKeys = cipherText.keyLengths.take(cipherText.keyLengths.size min 10).map { l => 
      val key = c*l; 
      (key, abs(heuristic.evaluate(key)-goal))
    }.sortBy(_._2)
    logger.trace("best initial keys: " + initialKeys.take(3))
    return initialKeys.head._1
  }
}
/*
 * Copyright 2011 Joshua Hollander.
 */

package org.jho.ace.ciphers

import org.jho.ace.util.Util._

class Vigenere(val key:String) {
  def encrypt(message:String):String = {
    message.view.filter(_.isLetter).map(_.toUpper).zipWithIndex.foldLeft("") {
      case(cipherText,(value,index)) => {
          cipherText + int2Char((char2Int(value) + char2Int(key(index % key.length))) mod 26)
        }
    }
  }

  def decrypt(cipherText:String):String = {
    cipherText.view.filter(_.isLetter).map(_.toUpper).zipWithIndex.foldLeft("") {
      case(message,(value,index)) => {
          message + int2Char((char2Int(value) - char2Int(key(index % key.length))) mod 26)
        }
    }
  }
}

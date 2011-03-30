/*
 * Copyright 2011 Joshua Hollander.
 */

package org.jho.ace.ciphers

import org.jho.ace.util.Util._

class Vigenere(val key:String) {
  def encrypt(message:String):String = {
    message.view.zipWithIndex.foldLeft("") {
      case(cipherText,(value,index)) => {
          cipherText + int2Char((char2Int(value) + char2Int(key(index % key.length))) mod 26)
        }
    }
  }

  def decrypt(cipherText:String):String = {
    cipherText.view.zipWithIndex.foldLeft("") {
      case(message,(value,index)) => {
          message + int2Char((char2Int(value) - char2Int(key(index % key.length))) mod 26)
        }
    }
  }
}

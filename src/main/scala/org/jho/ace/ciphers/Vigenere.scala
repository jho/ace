/*
 * Copyright 2011 Joshua Hollander.
 */

package org.jho.ace.ciphers

import org.jho.ace.util.Util._
import org.jho.ace.util.Language
import org.jho.ace.util.Configuration


class Vigenere(val key:String) extends Configuration {
  def encrypt(message:String)(implicit language:Language):String = {
    message.view.filter(_.isLetter).map(_.toUpper).zipWithIndex.foldLeft("") {
      case(cipherText,(value,index)) => {
          cipherText + language.int2Char((language.char2Int(value) + language.char2Int(key(index % key.length))) mod 26)
        }
    }
  }

  def decrypt(cipherText:String)(implicit language:Language):String = {
    cipherText.view.filter(_.isLetter).map(_.toUpper).zipWithIndex.foldLeft("") {
      case(message,(value,index)) => {
          message + language.int2Char((language.char2Int(value) - language.char2Int(key(index % key.length))) mod 26)
        }
    }
  }
}
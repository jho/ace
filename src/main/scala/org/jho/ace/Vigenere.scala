/*
 * Copyright 2011 Joshua Hollander.
 */

package org.jho.autodecrypt

import Implicits._

class Vigenere(val key:String) {
  def encrypt(m:String):String = {
    m.view.zipWithIndex.foldLeft("") { 
      case(c,(value,index)) => {
          c + int2Char((char2Int(value) + char2Int(key(index % key.length))) mod 26)
        }
    }
  }

  def decrypt(m:String):String = {
    m.view.zipWithIndex.foldLeft("") {
      case(c,(value,index)) => {
          c + int2Char((char2Int(value) - char2Int(key(index % key.length))) mod 26)
        }
    }
  }

  def char2Int(c:Char):Int = c.toInt - 65
  def int2Char(i:Int):Char = (i+65).toChar
}

class MyInt(i:Int) {
  def mod(j:Int):Int = {
    var rem = i % j
    if (rem < 0) {
      rem += j
    }
    return rem
  }
}

object Implicits {
  implicit def int2MyInt(i:Int):MyInt = new MyInt(i)
}
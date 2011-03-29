/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

object Util {
  def char2Int(c:Char):Int = c.toInt - 65
  def int2Char(i:Int):Char = (i+65).toChar
  implicit def int2MyInt(i:Int):MyInt = new MyInt(i)
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
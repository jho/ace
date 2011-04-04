/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

object Util {
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
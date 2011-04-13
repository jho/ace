/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

object Util {
  implicit def int2MyInt(i:Int):MyInt = new MyInt(i)
  implicit def int2MyBigInt(i:BigInt):MyBigInt = new MyBigInt(i)
}

class MyInt(i:Int) {
  //define a true modulus function (% just does a remainder)
  def mod(j:Int):Int = {

    var rem = i % j
    if (rem < 0) {
      rem += j
    }
    return rem
  }

  //define a factorial function
  def ! = (BigInt(1) /: (1 to i)) ( _ * _ )

  def times(f: => Unit):Unit = for ( j <- 1 to i) { f }
}

class MyBigInt(i:BigInt) {
  def ! = (BigInt(1) /: (BigInt(1) to i)) ( _ * _ )
}
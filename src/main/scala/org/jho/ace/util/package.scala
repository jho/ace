/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import scala.math._

package object util {
  implicit def int2MyInt(i:Int):MyInt = new MyInt(i)
  implicit def int2MyBigInt(i:BigInt):MyBigInt = new MyBigInt(i)
  implicit def string2MyString(s:String):MyString = new MyString(s)
  //why isn't this just part of the language?
  implicit def tupleToList[T](t:Product):List[T] = t.productIterator.toList map { case e:T => e}

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

    def times[A](f: => A):Traversable[A] = for ( j <- 1 to i) yield { f }
  }

  class MyBigInt(i:BigInt) {
    def ! = (BigInt(1) /: (BigInt(1) to i)) ( _ * _ )
  }

  class MyString(s:String) {
    /**
     * Compute Levenshtein distance of two strings
     *
     * Code originally from:
     * http://oldfashionedsoftware.com/2009/11/19/string-distance-and-refactoring-in-scala/
     */
    def distance(s2:String):Double = {
      def minimum(i1: Int, i2: Int, i3: Int) = min(min(i1, i2), i3)

      var dist = ( new Array[Int](s.length + 1),
                  new Array[Int](s.length + 1) )

      for (idx <- 0 to s.length) dist._2(idx) = idx

      for (jdx <- 1 to s2.length) {
        val (newDist, oldDist) = dist
        newDist(0) = jdx
        for (idx <- 1 to s.length) {
          newDist(idx) = minimum (
            oldDist(idx) + 1,
            newDist(idx-1) + 1,
            oldDist(idx-1) + (if (s(idx-1) == s2(jdx-1)) 0 else 1)
          )
        }
        dist = dist.swap
      }

      var res = dist._2(s.length).toDouble
      //turn the levenshtien distance in to a percentage
      (res/max(s.length(),s2.length()));
    }
  }
}
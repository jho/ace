/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import org.jho.ace.Keyword._
import org.jho.ace.util.Util._
import org.jho.ace.util.Configuration

class KeywordTest extends Configuration {
  @Test
  def neighborHood = {
    println("aa".neighbors)
    println("aa".neighbors.size)
    println("aa".neighbors(true))
    println("aa".neighbors(true).size)
    /*
     println("aaa".neighbors)
     println("aaa".neighbors.size)
     println("a".sizeOfNeighborhood)
     println("aa".sizeOfNeighborhood)*/
  }

  @Test
  def permutations = {
    println("KEYWORD".permutations.size)
  }

  @Test
  def mutate = {
    println("hello?")
    100.times {
      var result = "aa".mutate(true)
      println(result)
    }
    println("hello?")
  }
}

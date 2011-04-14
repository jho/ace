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
    println("aaa".neighbors)
    println("aaa".neighbors.size)
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
    5000.times {
      var result = "aaaa".mutate
      if ( result.size > 4 )
        println(result)
    }
    println("hello?")
  }
}

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
    println(5!)
    println(26!)
    /*var result = "a".sizeOfNeighborhood
    println(result)*/
    var result = "aa".sizeOfNeighborhood
    println(result)
  }

  @Test
  def permutations = {
    println("KEYWORD".permutations)
  }

  @Test
  def mutate = {
    100.times {
      println("aaaa".mutate)
    }
  }
}
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
    assertEquals(26, "a".sizeOfNeighborhood)
    assertEquals(326, "aa".sizeOfNeighborhood)
    assertEquals(2600, "aaa".sizeOfNeighborhood)
  }

  @Test
  def permutations = {
    println("KEYWORD".permutations)
  }

  @Test
  def mutate = {
    5.times {
      println("aaaa".mutate)
    }
  }
}

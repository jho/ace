/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import org.jho.ace.Keyword._
import org.jho.ace.util._
import org.jho.ace.util.Configuration

class KeywordTest extends Configuration {
  @Test
  def neighborHood = {
    assertEquals(50, "aa".neighbors.size)
    assertEquals(78, "aa".neighbors(true, true).size)
  }

  @Test
  def mutate = {
    100.times {
      var result = "aa".mutate(true)
      println(result)
    }
  }
}

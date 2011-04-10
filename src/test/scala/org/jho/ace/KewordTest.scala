/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import org.jho.ace.Keyword._
import org.jho.ace.util.Configuration

class KeywordTest extends Configuration {
  @Test
  def neighbors = {
    var result = "KEYWORD".neighbors
    println(result)
  }
}

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.heuristic

import org.jho.ace.util.Configuration

import org.junit._
import Assert._
import scala.math._

import org.jho.ace.util.Util._

class GramHeuristicTest extends Configuration {

  @Test
  def computeCost = {
    var g = new TrigramHeuristic(1.0)
    println("res: " + g.evaluate("This is a test of a valid English phrase which"))
    println("res: " + g.evaluate("garbageasdfasdfasdfasdfasfadsfasdfasdfasdfasdf"))
    println("res: " + g.evaluate("asdfasdfasd"))
    println("res: " + g.evaluate("theandworld"))
    //println(g.evaluate(language.sample(12)))
  }

  //@Test
  def computeBaseline = {
    var g = new TrigramHeuristic(1.0)
    var counts = 1000.times{ g.evaluate(language.sample(100)) }.toList
    println("Min: " + counts.min)
    println("Max: " + counts.max)
    var avg = counts.sum/counts.size
    println("Avg: " + avg)
    //println("Std Deviation: " + avg)
    println("Std Deviation: " + sqrt(counts.map(e => pow(avg - e, 2)).sum/counts.size))
  }
}

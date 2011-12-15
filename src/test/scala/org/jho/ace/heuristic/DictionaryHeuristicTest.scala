/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.heuristic

import org.junit._
import Assert._

import scala.math._

import org.jho.ace.util.Configuration
import org.jho.ace.util.Util._

class DictionaryHeuristicTest extends Configuration {

  @Test
  def computeCost = {
    var d = new DictionaryHeuristic(1.0)
    /*
    println(d.evaluate(("" /: (1 to 100)) { (s, i) => s + language.randomChar }))
    println(d.evaluate(("" /: (1 to 100)) { (s, i) => s + language.randomChar }))
    println(d.evaluate(language.sample(100)))
    println(d.evaluate(language.sample(100)))
    */
    def cost(text:String):Double = {
      ((4 to 10).foldLeft(0.0) { (sum, k) =>
          sum + (k^2 * (text.sliding(k).filter(language.dictionary.wordsUpperCase.contains(_)).toSet.size))
        })/text.size
    }
    var text = "HELLOWORLD"
    println(cost(text))
    println(cost("BLERGEDYI"))
    println(d.evaluate("HELLOYOU"))
    println(d.evaluate("BLERGEST"))
  }

  @Test
  def computeBaseline = {
    var d = new DictionaryHeuristic(1.0)
    var counts = 1000.times{ d.evaluate(language.sample(100)) }.toList
    println("Min: " + counts.min)
    println("Max: " + counts.max)
    var avg = counts.sum/counts.size
    println("Avg: " + avg)
    //println("Std Deviation: " + avg)
    println("Std Deviation: " + sqrt(counts.map(e => pow(avg - e, 2)).sum/counts.size))
  }
}

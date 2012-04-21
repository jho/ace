/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.heuristic

import org.junit._
import Assert._

import scala.math._

import org.jho.ace.util.Configureable
import org.jho.ace.util._

class DictionaryHeuristicTest extends Configureable {

  @Test
  def computeCost = {
    var d = new DictionaryHeuristic(1.0)
    var random = language.randomString(100)
    var words = random.sliding(4).withFilter(language.dictionary.wordsUpperCase.contains(_)).toSet
    var sample = language.sample(100)
    words = sample.sliding(4).withFilter(language.dictionary.wordsUpperCase.contains(_)).toSet
    println(4^2 * words.size)
    println(1.0/(4^2 * words.size))
    println(d.evaluate(language.sample(100)))
    println(d.evaluate(language.randomString(100)))
  }

  //@Test
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

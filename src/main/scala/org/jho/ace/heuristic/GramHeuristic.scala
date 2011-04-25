/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.heuristic

import org.jho.ace.util.Language
import org.jho.ace.util.Configuration
import org.jho.ace.CipherText._

import scala.math._

class GramHeuristic(weight:Double) extends Heuristic(weight) with Configuration {
  def compute(in:String):Double = {
    val text = in.filter(_.isLetter).toUpperCase
    var sum = gramSum(language.bigramFrequencies, text.bigramFrequencies)
    sum + (2.0 * gramSum(language.trigramFrequencies, text.trigramFrequencies))
  }

  def gramSum(expected:Map[String, Double], observed:Map[String, Double]):Double = {
    var sum = abs(expected.filter(e => observed.contains(e._1)).foldLeft(0.0) { (sum, e) =>
        sum + (observed(e._1) - e._2)
      })
    //grams that aren't in the string should count otherwise strings
    //that have no recognizable grams will actually have an artificially "lower" cost
    sum += abs(expected.filterNot(e => observed.contains(e._1)).foldLeft(0.0) { (sum, e) =>
        sum + e._2
      })
    sum
  }
}

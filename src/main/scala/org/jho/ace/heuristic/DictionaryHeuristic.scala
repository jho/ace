/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.heuristic

import org.jho.ace.util.Configuration
import org.jho.ace.util.Language

class DictionaryHeuristic(weight:Double) extends Heuristic(weight) with Configuration {
  def compute(in:Seq[Char]):Double = {
    val text = in.withFilter(_.isLetter).toString.toUpperCase
    (1.0/in.size)*((4 to 10).foldLeft(0.0) { (sum, k) =>
        sum + (k^2 * (text.sliding(k).withFilter(language.dictionary.wordsUpperCase.contains(_)).toSet.size))
      })
    //(in.size/language.avgWordSize)/sum
  }
}

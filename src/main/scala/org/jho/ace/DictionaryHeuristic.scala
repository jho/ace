/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Configuration
import org.jho.ace.util.Language

class DictionaryHeuristic(weight:Double) extends Heuristic(weight) with Configuration {
  def compute(in:String)(implicit language:Language):Double = {
    val text = in.filter(_.isLetter).toUpperCase
    var sum = (1.0/text.size)*((2 to 10).foldLeft(0.0) { (sum, k) =>
        sum + (k^2 * (text.sliding(k).filter(language.dictionary.wordsUpperCase.contains(_)).toSet.size))
      })
    (in.size/language.avgWordSize)/sum
  }
}

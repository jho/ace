/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Configuration
import org.jho.ace.util.Language

object DictionaryHeuristic extends Heuristic with Configuration {
  def apply(in:String)(implicit language:Language):Double = {
    var sum = (1.0/in.size)*((2 to 7).foldLeft(0.0) { (sum, k) =>
        sum + (k^2 * (in.sliding(k).filter(language.dictionary.wordsUpperCase.contains(_)).toSet.size))
      })
    sum
  }
}

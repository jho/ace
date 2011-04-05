/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Language
import org.jho.ace.util.Configuration
import org.jho.ace.CipherText._

import scala.math._

object GramHeuristic extends Heuristic with Configuration {
  def apply(in:String)(implicit language:Language):Double = {
    var sum = abs(in.frequencies.foldLeft(0.0) { (sum, e) =>
        sum + (language.frequencies(e._1) - e._2)
      })
    sum += gramSum(language.bigramFrequencies, in.bigramFrequencies)
    sum + gramSum(language.trigramFrequencies, in.trigramFrequencies)
  }

   def gramSum(expected:Map[String, Double], observed:Map[String, Double]):Double = {
    var sum = abs(expected.filter(e => observed.contains(e._1)).foldLeft(0.0) { (sum, e) =>
        sum + (expected(e._1) - e._2)
    })
    //grams that aren't in the string should count against it otherwise strings
    //that have no recognizable grams will actually have an artificially "lower" cost
    sum += abs(expected.filterNot(e => observed.contains(e._1)).foldLeft(0.0) { (sum, e) =>
        sum + (expected(e._1) - e._2)
    })
    sum
   }
}

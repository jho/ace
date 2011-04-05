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
    sum + abs(in.bigramFrequencies.filter(e => language.bigramFrequencies.contains(e._1)).foldLeft(0.0) { (sum, e) =>
        sum + (language.bigramFrequencies(e._1) - e._2)
      })
    sum + abs(in.trigramFrequencies.filter(e => language.trigramFrequencies.contains(e._1)).foldLeft(0.0) { (sum, e) =>
        sum + (language.trigramFrequencies(e._1) - e._2)
      })
    sum
  }
}

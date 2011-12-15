/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.heuristic

import org.jho.ace.util.Configuration
import org.jho.ace.CipherText._

import scala.math._

abstract class GramHeuristic(weight:Double) extends Heuristic(weight) with Configuration {
  def compute(in:Seq[Char]):Double = {
    doCompute(in.filter(_.isLetter).toString.toUpperCase)
  }

  protected def doCompute(in:String):Double
  /*
   val text = in.filter(_.isLetter).toUpperCase
   var sum = gramSum(language.bigramFrequencies, text.bigramFrequencies)
   sum + (2.0 * gramSum(language.trigramFrequencies, text.trigramFrequencies))
   }*/

  protected def gramSum(expected:Map[String, Double], observed:Map[String, Double]):Double = {
    println(observed)
    observed.foldLeft(0.0) { (sum, e) => 
      if(expected.contains(e._1)) {
        sum + (abs(expected(e._1) - e._2)/expected(e._1))
      } else {
        sum + e._2/.000000000000001
      }
    }
    /*
    var sum = abs(expected.filter(e => observed.contains(e._1)).foldLeft(0.0) { (sum, e) =>
        sum + (observed(e._1) - e._2)
      })
    //grams that aren't in the string should count otherwise strings
    //that have no recognizable grams will actually have an artificially "lower" cost
    sum += abs(expected.filterNot(e => observed.contains(e._1)).foldLeft(0.0) { (sum, e) =>
        sum + e._2
      })
    sum*/
  }
}

class BigramHeuristic(weight:Double) extends GramHeuristic(weight) {
  def doCompute(in:String) = {
    gramSum(language.bigramFrequencies, in.bigramFrequencies)
  }
}

class TrigramHeuristic(weight:Double) extends GramHeuristic(weight) {
  def doCompute(in:String) = {
    gramSum(language.trigramFrequencies, in.trigramFrequencies)
  }
}

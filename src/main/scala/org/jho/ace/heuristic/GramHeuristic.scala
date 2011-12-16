/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.heuristic

import org.jho.ace.util.Configuration
import org.jho.ace.CipherText._

import scala.math._

abstract class GramHeuristic(weight:Double) extends Heuristic(weight) with Configuration {
  def compute(in:String):Double = {
    doCompute(in.filter(_.isLetter).toUpperCase)
  }

  protected def doCompute(in:String):Double

  protected def gramSum(expected:Map[String, Double], observed:Map[String, Double]):Double = {
    observed.foldLeft(0.0) { (sum, e) => 
      if(expected.contains(e._1)) {
        var k = expected(e._1)
        sum + abs(k - e._2)
      } else {
        sum + 1 
      }
    }
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

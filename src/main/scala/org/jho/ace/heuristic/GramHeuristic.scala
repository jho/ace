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
      expected.get(e._1) match {
        case Some(x) => sum + abs(x - e._2)
        case None => sum + 1 
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

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Language
import org.jho.ace.util.Configuration

trait Cryptanalyzer extends Configuration {
  def decrypt(cipherText:String)(implicit language:Language):String

  /**
   *  Compute a baseline cost for a series of plaintexts in the
   *  given language that are the same length as the cipherText
   */
  protected def computeGoal(length:Int):Double = {
    var sum = (1 to 100).foldLeft(0.0) { (acc, w) =>
      var sample = language.sample(length)
      acc + heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(sample)}
    }
    sum/100
  }

  implicit object KeyCostTupleOrdering extends Ordering[(String, Double)] {
    def compare(x: (String, Double), y: (String, Double)):Int = y._2.compare(x._2)
  }
}

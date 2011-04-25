/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.ciphers.Cipher
import org.jho.ace.util.Configuration
import org.jho.ace.util.Util._

import scala.math._

trait Cryptanalyzer extends Configuration {
  def decrypt(cipherText:String, cipher:Cipher):CryptanalysisResult

  /**
   *  Compute a baseline cost for a series of plaintexts in the
   *  given language that are the same length as the cipherText
   */
  protected def computeGoal(length:Int):(Double, Double) = {
    var counts = 1000.times{  
      var sample = language.sample(length)
      heuristics.foldLeft(0.0)(_ + _.evaluate(sample))
    }.toList
    var avg = counts.sum/counts.size
    var stdDev = sqrt(counts.map(e => pow(avg - e, 2)).sum/counts.size)
    return (avg, stdDev)
  }

  implicit object KeyCostTupleOrdering extends Ordering[(String, Double)] {
    def compare(x: (String, Double), y: (String, Double)):Int = y._2.compare(x._2)
  }
}

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.cryptanalyzer

import org.jho.ace.heuristic._
import org.jho.ace.CipherText
import org.jho.ace.Key
import org.jho.ace.ciphers.Cipher
import org.jho.ace.util.Configureable
import org.jho.ace.util.LogHelper
import org.jho.ace.util._

import scala.collection.mutable.Map
import scala.math._

abstract class Cryptanalyzer(heuristic:Heuristic = Heuristic.default) extends Configureable with LogHelper {
  //memoize computed goals for different cipherText lenghts
  private val goals = Map.empty[Int, (Double, Double)]

  def decrypt[C <: CipherText](cipherText:C, cipher:Cipher[Key, C]):CryptanalysisResult

  def cost[C <: CipherText](text:CipherText):Double = {
    heuristic.evaluate(text)
  }

  /**
   *  Compute a baseline cost for a series of plaintexts in the
   *  given language that are the same length as the cipherText
   */
  protected def computeGoal(cipherText:CipherText):(Double, Double) = {
    goals.get(length) match {
      case Some(x) => x
      case None => {
          var counts = 2000.times{  
            var sample = language.sample(length)
            cost(sample)
          }.toList
          val avg = counts.sum/counts.size
          val stdDev = sqrt(counts.map(e => pow(avg - e, 2)).sum/counts.size)
          val res = (avg, stdDev)
          goals += length -> res
          res
        }  
    }
  }

  implicit object KeyCostTupleOrdering extends Ordering[(Key, Double)] {
    def compare(x: (Key, Double), y: (Key, Double)):Int = y._2.compare(x._2)
  }
}

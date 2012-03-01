/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.CipherText._
import org.jho.ace.Keyword._
import org.jho.ace.ciphers.Cipher
import org.jho.ace.util._

import scala.collection.mutable.HashSet
import scala.math._
import scala.util.Random

/**
 * Cryptanalyzer that uses a Simulated Annealing algorithm
 */
class SACryptanalyzer extends Cryptanalyzer {

  def decrypt(cipherText:String, cipher:Cipher):CryptanalysisResult = {
    var visited = new HashSet[String]()
    val rand = new Random();
    val (goal, stdDev) = computeGoal(cipherText.size)
    if ( logger.isTraceEnabled ) {
      logger.trace("goal: " + goal)
    }
    def cost(key:String):Double = {
      val decryption = cipher.decrypt(key, cipherText)
      heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(decryption)}
    }
    var key = language.frequencies.head._1.toString
    var best = (key, cost(key))
    var current = (key, cost(key))
    visited += key
    var max = pow(language.alphabet.size, cipherText.size)/2
    var temp = 100.0
    var i = 0
    while(abs(goal - best._2) > stdDev && i < SAConfig.outerLoops) {
      var change = false
      SAConfig.innerLoops.times {
        var n = current._1.mutate(true)
        if (!visited.contains(n)) {
          visited += n
          var next = (n, cost(n))
          val delta = abs(goal - next._2) - abs(goal - current._2)
          if ( delta <= 0 ) {
            current = next
          } else if ( rand.nextDouble < exp(-(delta)/temp) ) {
            current = next
          }
          if ( abs(goal - next._2) < abs(goal - best._2) ) {
            change = true
            best = next
            if ( logger.isTraceEnabled ) {
              logger.trace("new best:" + best)
            }
          }
        }
      }
      current = best
      temp = temp * SAConfig.coolingFactor
      if (!change) i += 1
    }
    new CryptanalysisResult(best._1, cipher.decrypt(best._1, cipherText), visited.size, best._2)
  }
}

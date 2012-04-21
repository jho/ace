/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.CipherText._
import org.jho.ace.Keyword._
import org.jho.ace.ciphers.Cipher
import org.jho.ace.heuristic.Heuristic
import org.jho.ace.util._

import scala.collection.mutable.HashSet
import scala.math._
import scala.util.Random

/**
 * Cryptanalyzer that uses a Simulated Annealing algorithm
 */
class SACryptanalyzer(heuristic:Heuristic = Heuristic.default, val config:SAConfig = new SAConfig) extends Cryptanalyzer(heuristic) {

  def decrypt(cipherText:String, cipher:Cipher):CryptanalysisResult = {
    var visited = new HashSet[String]()
    val rand = new Random();
    val (goal, stdDev) = computeGoal(cipherText.size)
    if ( logger.isTraceEnabled ) {
      logger.trace("goal: " + goal)
    }
    var key = cipher.generateInitialKey(cipherText, goal)
    logger.debug("Start state: " + key)
    var best = (key, cost(cipher.decrypt(key, cipherText)))
    var current = (key, cost(cipher.decrypt(key, cipherText)))
    visited += key
    var max = pow(language.alphabet.size, cipherText.size)/2
    var temp = config.startTemp
    var i = 0
    while(i < config.outerLoops && abs(goal - best._2) > 3.0*stdDev) {
      var change = false
      config.innerLoops.times {
        var n = current._1.mutate(true)
        if (!visited.contains(n)) {
          visited += n
          var next = (n, cost(cipher.decrypt(n, cipherText)))
          val delta = abs(goal - next._2) - abs(goal - current._2)
          if ( delta <= 0 ) {
            current = next
          } else if ( rand.nextDouble < exp(-(delta)/temp) ) {
            current = next
          }
          if ( abs(goal - next._2) < abs(goal - best._2) ) {
            change = true
            best = next
            logger.trace("new best:" + best)
          }
        }
      }
      current = best
      temp = temp * config.coolingFactor
      //if (!change) i += 1
    }
    new CryptanalysisResult(best._1, cipher.decrypt(best._1, cipherText), visited.size, best._2)
  }
}

class SAConfig {
  var startTemp = 100.0
  var coolingFactor = .97 
  var innerLoops = 300 
  var outerLoops = 500
}

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.CipherText._
import org.jho.ace.Keyword._
import org.jho.ace.ciphers.Cipher
import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Language
import org.jho.ace.util.Util._

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
    //println("goal: " + goal)
    def cost(key:String):Double = {
      val decryption = cipher.decrypt(key, cipherText)
      heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(decryption)}
    }
    //var key = cipher.generateInitialKey(cipherText)
    var key = language.frequencies.head._1.toString
    //println("starting key: " + key)
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
            //println("moving to state:" + next)
            current = next
          } else if ( rand.nextDouble < exp(-(delta)/temp) ) {
            //println("moving to probable state:" + next)
            current = next
          }
          if ( abs(goal - next._2) < abs(goal - best._2) ) {
            change = true
            best = next
            //println("new best: " + best)
          }
        }
      }
      current = best
      temp = temp * SAConfig.coolingFactor
      if (!change) i += 1
      //println("current temp: " + temp)
    }
    new CryptanalysisResult(best._1, cipher.decrypt(best._1, cipherText), visited.size, best._2)
  }
}

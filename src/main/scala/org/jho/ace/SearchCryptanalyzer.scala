/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.ciphers.Cipher
import org.jho.ace.util.Language
import org.jho.ace.CipherText._
import org.jho.ace.Keyword._

import scala.collection.mutable.HashSet
import scala.math._

/**
 * Simple steepest ascent hill-climbing.  Efficient if it doesn't get stuck on a local maxima, which it often does.
 */
class SearchCryptanalyzer extends Cryptanalyzer {
  var visited = new HashSet[String]()

  def decrypt(cipherText:String, cipher:Cipher):String = {
    var (goal, stdDev) = computeGoal(cipherText.size)
    println("goal: " + goal)
    def cost(key:String):Double = {
      val decryption = cipher.decrypt(key, cipherText)
      heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(decryption)}
    }
    var key = cipher.generateInitialKey(cipherText)
    println("starting key: " + key)
    var best = (key, cost(key))
    var i = 0
    while(abs(goal - best._2) > stdDev && visited.size <= maxIterations) {
      var mutation = best._1.mutate(true)
      if (!visited.contains(mutation)) {
        visited += mutation
        var next = (mutation, cost(mutation))
        if ( abs(goal - next._2) < abs(goal - best._2)) {
          best = next
          println("new best:" + best)
        }
      }
    }
    println("num keys searched: " + visited.size)
    println(best)
    return cipher.decrypt(best._1, cipherText)
  }
}

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Configuration
import org.jho.ace.util.Language
import org.jho.ace.CipherText._
import org.jho.ace.Keyword._

import scala.collection.mutable.HashSet
import scala.math._

/**
 * Cryptanalyzer that uses a Simulated Annealing algorithm
 */
class SACryptanalyzer extends Cryptanalyzer {
  var visited = new HashSet[String]()

  def decrypt(cipherText:String)(implicit language:Language):String = {
    var goal = computeGoal(cipherText.size)
    println("goal: " + goal)
    def cost(key:String):Double = {
      val decryption = new Vigenere(key).decrypt(cipherText)
      heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(decryption)}
    }
    val keyLength = cipherText.keyLengths.head
    var key = ("" /: (1 to keyLength)) { (s, i) => s + language.frequencies.head._1 }
    println("starting key: " + key)
    var best = (key, cost(key))
    var i = 0
    var max = pow(language.alphabet.size, key.size)/2
    while(abs(goal._1 - best._2) > goal._2 && visited.size <= max) {
      var mutation = best._1.mutate
      if (!visited.contains(mutation)) {
        visited += mutation
        var next = (mutation, cost(mutation))
        if ( next._2 < best._2) {
          best = next
          println("new best:" + best)
        }
      }
    }
    println("num keys searched: " + visited.size)
    println(best)
    return new Vigenere(best._1).decrypt(cipherText)
  }
}
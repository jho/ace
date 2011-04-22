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
  val rand = new Random();
  var visited = new HashSet[String]()

  def decrypt(cipherText:String, cipher:Cipher)(implicit language:Language):String = {
    val goal = computeGoal(cipherText.size)
    println("goal: " + goal)
    def cost(key:String):Double = {
      val decryption = cipher.decrypt(key, cipherText)
      heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(decryption)}
    }
    var key = ("" /: (1 to cipherText.keyLengths.head)) { (s, i) => s + language.frequencies.head._1 }
    println("starting key: " + key)
    var best = (key, cost(key))
    var current = (key, cost(key))
    var max = pow(language.alphabet.size, cipherText.size)/2
    var temp = 100.0
    SAConfig.outerLoops.times {
      SAConfig.innerLoops.times {
        var n = current._1.mutate
        if (!visited.contains(n)) {
          visited += n
          var next = (n, cost(n))
          val delta = next._2 - current._2
          if ( delta <= 0 ) {
            //println("moving to state:" + current)
            current = next
          } else if ( rand.nextDouble < exp(-(delta)/temp) ) {
            //println("moving to probable state:" + current)
            current = next
          }
          if ( next._2 < best._2) {
            best = next
            println("new best: " + best)
          }
        }
      }
      temp = temp * 0.95
      println("current temp: " + temp)
      println(abs(goal._1 - best._2)+" > "+goal._2)
    }
    println("num keys searched: " + visited.size)
    println(best)
    return cipher.decrypt(best._1, cipherText)
  }
}

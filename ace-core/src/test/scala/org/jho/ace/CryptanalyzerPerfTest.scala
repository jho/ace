/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import org.jho.ace.ciphers.Cipher
import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Configuration
import org.jho.ace.util.Util._

class CryptanalyzerPerfTest extends Configuration {
  @Test
  def performanceTest = {
    var astar = new AStarCryptanalyzer
    var sa = new SACryptanalyzer
    var cipher = new Vigenere
    println("-----------------------")
    println("100-char, 5-char keyword")
    println("A* stats: " + gatherStats(100, "LEMON", astar, cipher))
    println("SA stats: " + gatherStats(100, "LEMON", sa, cipher))
    println("-----------------------")
    println("-----------------------")
    println("150-char, 5-char keyword")
    println("A* stats: " + gatherStats(150, "LEMON", astar, cipher))
    println("SA stats: " + gatherStats(150, "LEMON", sa, cipher))
    println("-----------------------")
    println("-----------------------")
    println("200-char, 5-char keyword")
    println("A* stats: " + gatherStats(200, "LEMON", astar, cipher))
    println("SA stats: " + gatherStats(200, "LEMON", sa, cipher))
    println("-----------------------")
  }

  private def gatherStats(size:Int, key:String, ca:Cryptanalyzer, cipher:Cipher):(Int, Double, Long) = {
    def run(plainText:String):(Int, Double, Long) = {
      var cipherText = cipher.encrypt(key, plainText)
      var startTime = System.currentTimeMillis
      var result = ca.decrypt(cipherText, cipher)
      (result.numKeysSearched, plainText.diff(result.plainText), (System.currentTimeMillis - startTime))
    }
    var results = List[(Int, Double, Long)]()
    10.times {
      results = run(language.sample(size)) :: results
    }
    var sum = results.foldLeft((0, 0.0, 0L)) { (sum, res) =>
      (sum._1 + res._1, sum._2 + (1.0 - res._2), sum._3 + res._3)
    }
    ((sum._1/results.size), (sum._2/results.size), (sum._3/results.size))
  }
}

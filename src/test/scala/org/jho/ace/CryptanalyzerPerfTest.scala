/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import org.jho.ace.ciphers.Cipher
import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Configuration
import org.jho.ace.util._

class CryptanalyzerPerfTest extends Configuration {
  @Test
  def performanceTest = {
    var astar = new AStarCryptanalyzer
    var sa = new SACryptanalyzer
    var cipher = new Vigenere

    var keys = (4 to 10).map(language.dictionary.randomWord(_))
    var sizes = (50 to 200 by 50)
    var algorithms = List(("A*", astar), ("SA", sa))
    var runs = algorithms.map { case (name, algorithm) => 
        println("algorithm: "+name)
        (name, (keys.zip(sizes).map { case (key, size) => 
                println("-----------------------")
                println(size+"-char, "+key.length+"-char keyword: "+key)
                println("-----------------------")
                run(size, key, algorithm, cipher)
            }))
    }
    println(runs.toMap)
  }

  private def run(size:Int, key:String, ca:Cryptanalyzer, cipher:Cipher):(Int, Int, Int, Double, Long) = {
    def run(plainText:String):(Int, Double, Long) = {
      var cipherText = cipher.encrypt(key, plainText)
      var startTime = System.currentTimeMillis
      var result = ca.decrypt(cipherText, cipher)
      (result.numKeysSearched, plainText.distance(result.plainText), (System.currentTimeMillis - startTime))
    }
    var results = List[(Int, Double, Long)]()
    5.times {
      results = run(language.sample(size)) :: results
    }
    //results.foreach(println(_))
    var sum = results.foldLeft((0, 0.0, 0L)) { (sum, res) =>
      (sum._1 + res._1, sum._2 + (1.0 - res._2), sum._3 + res._3)
    }
    (key.length(), size, (sum._1/results.size), (sum._2/results.size), (sum._3/results.size))
  }
}

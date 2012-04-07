/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import org.apache.commons.lang.builder.ToStringStyle
import org.jho.ace.ciphers.Cipher
import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Configuration
import org.jho.ace.util.Plotter
import org.jho.ace.util._
import org.apache.commons.lang.builder.ToStringBuilder

object PerformanceAnalyzer extends Configuration with LogHelper {
  def main(args:Array[String]) = {
    var astar = new AStarCryptanalyzer
    var sa = new SACryptanalyzer
    var cipher = new Vigenere

    var keys = (4 to 5).map(language.dictionary.randomWord(_))
    var sizes = (50 to 50 by 50)
    var runs = keys.foldLeft(List[(String, Int)]())((runs, key) => runs ++ sizes.map(size => (key, size)))
    logger.debug(runs)
    var algorithms = List(("A*", astar))//, ("SA", sa))
    var results = algorithms.map { case (name, algorithm) => 
        println("algorithm: "+name)
        (name, (runs.map { case (key, size) => 
                println("-----------------------")
                println(size+"-char, "+key.length+"-char keyword: "+key)
                println("-----------------------")
                run(size, key, algorithm, cipher)
            }))
    }
    logger.debug(results)
    results.foreach { result => 
      var grouped = result._2.groupBy(res => res.keySize) //group by key length
      var plotter = new Plotter("Accuracy", "Ciphertext size")
      logger.debug("grouped: " + grouped)
      grouped.foreach{ case(size, results) => 
        var data = results.map(res => (res.accuracy, res.textSize))
        logger.debug("Data: " + data)
        plotter.plot(data, Map("title" -> size.toString, "with" -> "linespoints"))
      }
      plotter.run
    }
    println("Done")
  }

  class Result(var keySize:Int, var textSize:Int, var keysSearched:Double, var accuracy:Double, var runTime:Double) {
    override def toString:String = {
      return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE)
    }
  }

  private def run(size:Int, key:String, ca:Cryptanalyzer, cipher:Cipher):Result = {
    def run(plainText:String):(Int, Double, Long) = {
      var cipherText = cipher.encrypt(key, plainText)
      var startTime = System.currentTimeMillis
      var result = ca.decrypt(cipherText, cipher)
      println(plainText)
      println(result)
      (result.numKeysSearched, plainText.distance(result.plainText), (System.currentTimeMillis - startTime))
    }
    var results = List[(Int, Double, Long)]()
    1.times {
      results = run(language.sample(size)) :: results
    }
    results.foreach(println(_))
    var sum = results.foldLeft((0, 0.0, 0L)) { (sum, res) =>
      (sum._1 + res._1, sum._2 + (1.0 - res._2), sum._3 + res._3)
    }
    new Result(key.length(), size, (sum._1/results.size), (sum._2/results.size), (sum._3/results.size))
  }
}

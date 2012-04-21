/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.performance

import org.junit._
import Assert._

import org.apache.commons.lang.builder.ToStringStyle
import org.jho.ace.ciphers.Cipher
import org.jho.ace.ciphers.Vigenere
import org.jho.ace._
import org.jho.ace.util.Configureable
import org.jho.ace.util.Plotter
import org.jho.ace.util._
import org.apache.commons.lang.builder.ToStringBuilder

class PerformanceAnalyzer extends Configureable with LogHelper {
  def run() = {
    var astar = new AStarCryptanalyzer
    var sa = new SACryptanalyzer
    var cipher = new Vigenere

    //var keys = (4 to 10 by 2).map(language.dictionary.randomWord(_))
    var keyLengths = (4 to 4 by 2)
    var sizes = (50 to 100 by 50)
    //var sizes = (50 to 500 by 50)
    var runs = keyLengths.foldLeft(List[(Int, Int)]())((runs, keyLength) => runs ++ sizes.map(size => (keyLength, size)))
    logger.debug(runs)
    var algorithms = List(("A*", astar))//, ("SA", sa))
    var results = algorithms.map { case (name, algorithm) => 
        logger.debug("algorithm: "+name)
        (name, (runs.map { case (keyLength, size) => 
                runCryptanalyzer(size, keyLength, algorithm, cipher)
            }))
    }
    logger.debug(results)
    results.foreach { result => 
      var grouped = result._2.groupBy(res => res.keySize) //group by key length
      var plotter = new Plotter(result._1 + " Accuracy")
      plotter.xlabel = "Ciphertext size"
      plotter.ylabel = "Accuracy"
      plotter.xrange = (0.0, sizes.last)
      plotter.yrange = (0.0, 1.1)
      grouped.zipWithIndex.foreach{ case((size, results), i) => 
          var data = results.map(res => (res.textSize.toDouble, res.accuracy)).toSeq
          logger.debug("data: " + size + " -> " + data)
          plotter.plot(data, size.toString+"-characters", "w lp ls "+(i+1))
      }
      plotter.run

      plotter = new Plotter(result._1 + " Runtime") 
      plotter.xlabel = "Ciphertext size"
      plotter.ylabel = "Runtime"
      //plotter.xrange = (0.0, sizes.last)
      //plotter.yrange = (0.0, 1.1)
      grouped.zipWithIndex.foreach{ case((size, results), i) => 
          var data = results.map(res => (res.textSize.toDouble, res.runTime)).toSeq
          plotter.plot(data, size.toString+"-characters", "w lp ls "+(i+1))
      }
      plotter.run

      plotter = new Plotter(result._1 + " Keys explored") 
      plotter.xlabel = "Ciphertext size"
      plotter.ylabel = "Keys explored"
      //plotter.xrange = (0.0, sizes.last)
      //plotter.yrange = (0.0, 1.1)
      grouped.zipWithIndex.foreach{ case((size, results), i) => 
          var data = results.map(res => (res.textSize.toDouble, res.keysSearched)).toSeq
          plotter.plot(data, size.toString+"-characters", "w lp ls "+(i+1))
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

  protected def runCryptanalyzer(size:Int, keyLength:Int, ca:Cryptanalyzer, cipher:Cipher):Result = {
    logger.debug(size+"-char, "+keyLength+"-char keyword")
    def run(plainText:String):(Int, Double, Long) = {
      var key = language.dictionary.randomWord(keyLength)
      var cipherText = cipher.encrypt(key, plainText)
      var startTime = System.currentTimeMillis
      var result = ca.decrypt(cipherText, cipher)
      (result.numKeysSearched, plainText.distance(result.plainText), (System.currentTimeMillis - startTime))
    }
    var results = 4.times {
      run(language.sample(size))
    }.toList
    //drop the worst result (to increase consistency between runs)
    results = results.sortBy(_._2).take(results.size-1)
    logger.debug("Results for "+size+"-char, "+keyLength+"-char keyword: " + results)
    var sum = results.foldLeft((0, 0.0, 0L)) { (sum, res) =>
      (sum._1 + res._1, sum._2 + (1.0 - res._2), sum._3 + res._3)
    }
    results = results.sortWith(_._2 < _._2)
    new Result(keyLength, size, (sum._1/results.size), (sum._2/results.size), (sum._3/results.size))
  }

  protected def plot(title:String, xlabel:String, ylabel:String, data:Map[String, Seq[(Double, Double)]], xrange:(Double, Double) = null, yrange:(Double, Double) = null) = {
    var plotter = new Plotter(title)
    plotter.xlabel = xlabel
    plotter.ylabel = ylabel
    if(xrange != null) plotter.xrange = xrange
    if(yrange != null) plotter.yrange = yrange
    data.zipWithIndex.foreach{ case((series, results), i) => 
        //logger.debug("data: " + series + " -> " + data)
        plotter.plot(results, series.toString+"-characters", "w lp ls "+(i+1))
    }
    plotter.run
  }
}

object PerformanceAnalyzer {
  def main(args:Array[String]) = {
    new PerformanceAnalyzer().run()
  }
}

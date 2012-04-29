/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.analyzer

import org.junit._
import Assert._

import org.jho.ace.ciphers.Vigenere
import org.jho.ace.cryptanalyzer._
import org.jho.ace.util.Plotter
import org.jho.ace.util._

class PerformanceAnalyzer extends AnalyzerBase { 
  collection.parallel.ForkJoinTasks.defaultForkJoinPool.setParallelism(4)
  def run() = {
    val cipher = new Vigenere

    //var keyLengths = (5 to 15 by 5)
    var keyLengths = List(10)
    var textLengths = (100 to 1000 by 100)
    val algorithms = List(("AC*", new BFSCryptanalyzer), ("SA", new SACryptanalyzer), ("GA", new GeneticCryptanalyzer))
    //val algorithms = List(("AC*", new BFSCryptanalyzer))

    keyLengths.foreach { keyLength => 
      var plotter = new Plotter("Key space explored with key length " + keyLength)
      plotter.xlabel = "Ciphertext size"
      plotter.ylabel = "Accuracy"
      plotter.xrange = (0.0, textLengths.last)
      plotter.yrange = (0.0, 1.1)

      algorithms.zipWithIndex.par.foreach { case ((name, algorithm), i) => 
          var results = textLengths.map { textLength => 
            runCryptanalyzer(textLength, keyLength, algorithm, cipher)
          }
          var data = results.map(res => (res.textSize.toDouble, res.keysSearched)).toSeq
          logger.debug("data: " + name + " -> " + data)
          plotter.plot(data, name, "w lp ls "+(i+1))
      } 
      plotter.run
    } 
    println("Done")
  } 
}

object PerformanceAnalyzer {
  def main(args:Array[String]) = {
    new PerformanceAnalyzer().run()
  }
}

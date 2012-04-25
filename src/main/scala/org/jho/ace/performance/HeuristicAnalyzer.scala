/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.performance

import org.junit._
import Assert._

import org.jho.ace.AStarCryptanalyzer
import org.jho.ace.GeneticCryptanalyzer
import org.jho.ace.SACryptanalyzer
import org.jho.ace.ciphers.Vigenere
import org.jho.ace.heuristic._
import org.jho.ace.util.Plotter
import org.jho.ace.util._
import scala.collection.parallel._

import scala.math._

class HeuristicAnalyzer extends PerformanceAnalyzer {
  collection.parallel.ForkJoinTasks.defaultForkJoinPool.setParallelism(4)
  override def run() = {
    //val cryptanalyzer = new AStarCryptanalyzer 
    //val cryptanalyzer = new GeneticCryptanalyzer 
    var cipher = new Vigenere

    var sizes = (100 to 1000 by 100).toList
    var gridRange = (1 to 3)
    /*
    var grid = for(i <- gridRange; j <- gridRange; k <- gridRange) yield {
      List(pow(2, i), pow(2, j), pow(2, k))
    }*/
    var grid = List(List(2.0, 2.0, 2.0), List(4.0, 2.0, 4.0))
    logger.debug(grid.size)
    var results = grid.map { weights => 
        logger.debug("Current weights: " + weights)
        val cryptanalyzer = new GeneticCryptanalyzer(heuristic = List(new DictionaryHeuristic(weights(0)),    
                                                                 new BigramHeuristic(weights(1)), new TrigramHeuristic(weights(2))))
        var res = sizes.par.map(runCryptanalyzer(_, 10, cryptanalyzer, cipher)).seq
        var avgAccuracy = res.foldLeft(0.0)((sum, e) => sum + e.accuracy)/res.size
        var avgKeys = res.foldLeft(0.0)((sum, e) => sum + e.keysSearched)/res.size
        var result = (weights, avgAccuracy, avgKeys, res)
        logger.debug("Result: " + result)
        result
    }.seq

    //sort by average accuracy and keys searched
    results = results.sortWith(_._2 > _._2)
    results.foreach(x => println((x._1, x._2, x._3)))

    //graph the top 4 results
    results = results.take(4)
    var data = results.map(res => (res._1.mkString(", "), res._4.map(i => (i.textSize.toDouble, i.accuracy)))).toMap
    plot("Accuracy", "Ciphertext size", "Accuracy", data, (0.0, sizes.last+50), (0.0, 1.1))
    data = results.map(res => (res._1.mkString(", "), res._4.map(i => (i.textSize.toDouble, i.keysSearched)))).toMap
    plot("Keys Explored", "Ciphertext size", "Keys explored", data, (0.0, sizes.last+50), (0.0, 1.1))
  }
}

object HeuristicAnalyzer {
  def main(args:Array[String]) = {
    new HeuristicAnalyzer().run()
  }
}



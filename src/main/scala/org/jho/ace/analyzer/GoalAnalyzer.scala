/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.analyzer

import org.junit._
import Assert._

import org.jho.ace.ciphers.Vigenere
import org.jho.ace.heuristic._
import org.jho.ace.util._

import scala.math._

class GoalAnalyzer extends AnalyzerBase {
  collection.parallel.ForkJoinTasks.defaultForkJoinPool.setParallelism(2)
  def run() = {
    val cipher = new Vigenere

    var heuristics = List[Heuristic]()
    def cost(text:String):Double = {
      heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(text)}
    }
    val gridRange = 1 to 1
    val grid = for(i <- gridRange; j <- gridRange; k <- gridRange) yield {
      ((pow(2, i), pow(2, j), pow(2, k)))
    }
    var results = grid.par.flatMap{ weights => 
      println(weights)
      heuristics = List(new DictionaryHeuristic(weights._1), 
        new IoCHeuristic(weights._2), new TrigramHeuristic(weights._3))
      var length = 100
      var counts = 10000.times { 
        var sample = language.sample(length)
        cost(sample)
      }.toList
      plot("Distribution of the Cost function", "Cost", "Frequency", 
        Map("100 characters" -> counts.groupBy(identity).map(x => (x._1,
        x._2.size*1.0)).toSeq))
      System.exit(-1)
      val avg = counts.sum/counts.size
      val stdDev = sqrt(counts.map(e => pow(avg - e, 2)).sum/counts.size)
      (weights, (2.0 to 4.0 by 0.5) map { multiplier => 
        var outliers = 2000.times { 
          var correct = language.sample(length)
          abs(cost(correct) - avg) <= (stdDev*multiplier)
        }.filter(_ == false).size
        var inliers = 2000.times { 
          var correct = language.sample(length).randomMutation
          abs(cost(correct) - avg) >= (stdDev*multiplier)
        }.filter(_ == false).size
        (multiplier, outliers, inliers)
      })
    }.seq
    println(results)
    //println(results.sortBy(x => (x._5, x._6)).take(10))
  }
}

object GoalAnalyzer {
  def main(args:Array[String]) = {
    new GoalAnalyzer().run()
  }
}



/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.ciphers.Cipher
import org.jho.ace.util.Language
import org.jho.ace.CipherText._
import org.jho.ace.Keyword._

import scala.collection.mutable.HashMap
import scala.collection.mutable.PriorityQueue
import scala.math._

/**
 * A Cryptanalyzer algorithm based on the A* path finding algorithm (or Best First Search with a heuristic)
 */
class AStarCryptanalyzer extends Cryptanalyzer {

  def decrypt(cipherText:String, cipher:Cipher):CryptanalysisResult = {
    var queue = new PriorityQueue[(String, Double)]()
    var visited = new HashMap[String, Boolean]()
    var cost = new HashMap[String, Double]().withDefaultValue(0);
    var (goal, stdDev) = computeGoal(cipherText.size)
    if ( logger.isTraceEnabled ) {
      logger.trace("goal: " + goal + " +/- " + stdDev)
    }
    def dist(decryption:String):Double = {
      heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(decryption)}
    }
    var start = language.frequencies.head._1.toString
    var best = (start, dist(cipher.decrypt(start, cipherText)))
    queue += ((best._1, abs(goal - best._2)))
    logger.debug("start: " + queue);
    var count = 0;
    while(!queue.isEmpty && visited.size <= maxIterations) {
      var next = queue.dequeue
      visited += next._1 -> true
      next._1.neighbors(true, true).withFilter(!visited.contains(_)).foreach { n =>
        val decryption = cipher.decrypt(n, cipherText)
        var d = dist(decryption)
        var c = cost(next._1) + d
        var h = abs(goal-d)
        /*
         if ( logger.isTraceEnabled ) {
         logger.trace("checking:" + (n, h) + "->" + c)
         }*/
        if (!cost.contains(n) || c < cost(n)) {
          cost += n -> c
          queue += n -> h  
        }
        //record the best if we have seen it
        if(d < best._2) {
          best = (n, d)
          if ( logger.isTraceEnabled ) {
            logger.trace("new best:" + best)
            logger.trace("iterations since last best: " + count)
          }
          count = 0
          //have we reached the goal?
          if(abs(goal - best._2) <= stdDev) {
            return new CryptanalysisResult(best._1, cipher.decrypt(best._1, cipherText), visited.size, best._2)
          }
        }
      }
      count += 1
    }
    return new CryptanalysisResult(best._1, cipher.decrypt(best._1, cipherText), visited.size, best._2)
  }
}

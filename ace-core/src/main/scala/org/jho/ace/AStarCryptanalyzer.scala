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
    var visited = new HashMap[String, Double]()
    var (goal, stdDev) = computeGoal(cipherText.size)
    if ( logger.isTraceEnabled ) {
      logger.trace("goal: " + (goal, stdDev))
    }
    def dist(decryption:String):Double = {
      heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(decryption)}
    }
    var best = language.frequencies.head._1.toString
    var decryption = cipher.decrypt(best, cipherText)
    queue += ((best, abs(goal - dist(decryption))))
    visited += best -> dist(decryption)
    while(abs(goal - visited(best)) > stdDev && !queue.isEmpty && visited.size <= maxIterations) {
      var next = queue.dequeue
      next._1.neighbors(true, true).withFilter(!visited.contains(_)).foreach { n =>
        val decryption = cipher.decrypt(n, cipherText)
        //no need check if one path is shorter than another like in standard A*
        //we don't care about the cost of the path as much as the cost of each node
        var d = dist(decryption)
        var c = abs(goal-d)
        queue += n -> c
        visited += n -> d
        if ( abs(goal-d) < abs(goal-visited(best)) ) {
          if ( logger.isTraceEnabled ) {
            logger.trace("new best:" + (n, c) + "->" + d)
          }
          best = n
        }
      }
    }
    new CryptanalysisResult(best, cipher.decrypt(best, cipherText), visited.size, visited(best))
  }
}

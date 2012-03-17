/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.ciphers.Cipher
import org.jho.ace.CipherText._
import org.jho.ace.Keyword._
import org.jho.ace.classifier.GramSvm2

import scala.collection.mutable.HashMap
import scala.collection.mutable.PriorityQueue
import scala.math._

/**
 * A Cryptanalyzer algorithm based on the A* path finding algorithm (or Best First Search with a heuristic)
 */
class AStarCryptanalyzer extends Cryptanalyzer {
  val classifier = new GramSvm2
  classifier.load

  def decrypt(cipherText:String, cipher:Cipher):CryptanalysisResult = {
    var queue = new PriorityQueue[(String, Double)]()
    var visited = new HashMap[String, Boolean]()
    var cost = new HashMap[String, Double]() {
        override def default(k: String) = 0.0
    }
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
        if (!cost.contains(n) || c < cost(n)) {
          cost += n -> c
          queue += n -> abs(goal-d)  
        }
        //record the best if we have seen it
        if(d < best._2) {
          best = (n, d)
          logger.debug("new best:" + best)
          logger.trace("iterations since last best: " + count)
          logger.debug("Score: " + classifier.score(decryption))
          logger.debug("Classification: " + classifier.classify(decryption))
          //have we reached the goal?
          if(classifier.classify(decryption) /*|| abs(goal - best._2) <= (stdDev * 3.0)*/) {
            return new CryptanalysisResult(best._1, cipher.decrypt(best._1, cipherText), count, best._2)
          }
        }
        count += 1
      }
    }
    return new CryptanalysisResult(best._1, cipher.decrypt(best._1, cipherText), count, best._2)
  }
}

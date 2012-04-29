/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.cryptanalyzer

import org.jho.ace.ciphers.Cipher
import org.jho.ace.CipherText._
import org.jho.ace.Keyword._
import org.jho.ace.classifier.DictionarySvm
import org.jho.ace.heuristic.Heuristic
import org.jho.ace.util._

import scala.collection.mutable.HashMap
import scala.collection.mutable.PriorityQueue
import scala.math._

/**
 * A Cryptanalyzer algorithm based on the A* path finding algorithm (or Best First Search with a heuristic)
 */
class BFSCryptanalyzer(heuristic:Heuristic = Heuristic.default) extends Cryptanalyzer(heuristic) {
  val classifier = new DictionarySvm
  //classifier.load

  def decrypt(cipherText:String, cipher:Cipher):CryptanalysisResult = {
    val queue = new PriorityQueue[(String, Double)]()
    val visited = new HashMap[String, Boolean]()
    val dist = new HashMap[String, Double]() {
      override def default(k: String) = Double.MaxValue
    }
    val (goal, stdDev) = computeGoal(cipherText.size)
    logger.debug("goal: " + goal + " +/- " + 3.0*stdDev)
    def isGoal(cost:Double):Boolean = {
      return abs(goal - cost) <= (stdDev * 3.0)
    }
    val maxIterations = 200000 //TODO: make this a function of the keyspace size
    val start = cipher.generateInitialKey(cipherText, goal)
    //val start = language.frequencies.head._1.toString
    logger.debug("Start state: " + start)
    var best = (start, cost(cipher.decrypt(start, cipherText)))
    queue += (best)
    dist(best._1) = 0
    logger.debug("start: " + queue);
    var count = 0
    var sinceBest = 0
    while(!queue.isEmpty && count <= maxIterations && !isGoal(best._2)) {
      logger.trace("open list: " + queue.take(100))
      val next = (queue.dequeue)
      visited += next._1 -> true
      if(sinceBest % 1000 == 0) {
        logger.trace("Keys since last best: "+sinceBest)
        logger.trace(queue.take(100))
      }
      next._1.neighbors(true, true).withFilter(n => !(visited.contains(n)) && !(dist.contains(n))).foreach { n =>
        val decryption = cipher.decrypt(n, cipherText)
        val c = cost(decryption)
        dist += n -> c 
        queue += ((n, c))
        sinceBest += 1 
        //record the best if we have seen it
        if(c < best._2) {
          sinceBest = 0
          best = (n, c)
          logger.trace("new best:" + best)
          logger.trace(count+" keys searched")
          //logger.debug("Score: " + classifier.score(decryption))
          //logger.debug("Classification: " + classifier.classify(decryption))
          //have we reached the goal?
          //if(classifier.classify(decryption)) { 
          if(isGoal(best._2)) {
            logger.info("exiting with best: " + best)
            return new CryptanalysisResult(best._1, decryption, count, best._2)
          }
        }
        count += 1
      }
    }
    return new CryptanalysisResult(best._1, cipher.decrypt(best._1, cipherText), count, best._2)
  }
}

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

  def decrypt(cipherText:String, cipher:Cipher):String = {
    var queue = new PriorityQueue[(String, Double)]()
    var visited = new HashMap[String, Double]()
    var (goal, stdDev) = computeGoal(cipherText.size)
    println("goal: " + (goal, stdDev))
    def cost(decryption:String):Double = {
      abs(goal - dist(decryption))
    }
    def dist(decryption:String):Double = {
      heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(decryption)}
    }
    var best = cipher.generateInitialKey(cipherText)//language.frequencies.head._1.toString
    println("starting key: " + best)
    var decryption = cipher.decrypt(best, cipherText)
    queue += ((best, cost(decryption)))
    visited += best -> dist(decryption)
    while(abs(goal - visited(best)) > stdDev && !queue.isEmpty && visited.size <= maxIterations) {
      var next = queue.dequeue
      //println("checking neighbors of:" + next)
      next._1.neighbors(true, true).filterNot(visited.contains(_)).filterNot(queue.contains(_)).foreach { n =>
        val decryption = cipher.decrypt(n, cipherText)
        //the graph is a tree and there is only a single path to each node
        //so we can just add all the neighbors t the queue
        //and not check if one path is shorter than another like in standard A*
        var c = cost(decryption)
        queue += n -> c
        //we don't care about the cost of the path as much as the cost of each node
        var d = dist(decryption)
        //println("checking:" + n + "->" + d)
        visited += n -> d
        if ( abs(goal-d) < abs(goal-visited(best)) ) {
          println("new best:" + (n, c) + "->" + d)
          best = n
        }
      }
      //println(queue)
    }
    println("num keys searched: " + visited.size)
    println("best: " + (best, visited(best)))
    return cipher.decrypt(best, cipherText)
  }
}

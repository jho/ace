/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Language
import org.jho.ace.CipherText._
import org.jho.ace.Keyword._

import scala.collection.mutable.HashMap
import scala.collection.mutable.PriorityQueue
import scala.math._

class AStaredCryptanalyzer extends Cryptanalyzer {

  def decrypt(cipherText:String)(implicit language:Language):String = {
    var queue = new PriorityQueue[(String, Double)]()
    var visited = new HashMap[String, Double]()
    var goal = computeGoal(cipherText.size)
    println("goal: " + goal)
    def cost(decryption:String):Double = {
      abs(goal - dist(decryption))
    }
    def dist(decryption:String):Double = {
      heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(decryption)}
    }
    var best = language.frequencies.head._1.toString
    println("starting key: " + best)
    var decryption = new Vigenere(best).decrypt(cipherText)
    queue += ((best, cost(decryption)))
    visited += best -> dist(decryption)
    var max = pow(language.alphabet.size, cipherText.size)/2 //search no more than half the keyspace
    while(abs(goal - visited(best)) > .1 && !queue.isEmpty && visited.size <= max) {
      var next = queue.dequeue
      println("checking neighbors of:" + next)
      next._1.neighbors(true).filterNot(visited.contains(_)).foreach { n =>
        val decryption = new Vigenere(n).decrypt(cipherText)
        //the graph is a tree and there is only a single path to each node
        //so we can just add all the neighbors t the queue
        //and not check if one path is shorter than another like in standard A*
        var c = cost(decryption)
        queue += n -> c
        //we don't care about the cost of the path as much as the cost of each node
        var d = dist(decryption)
        //println("checking:" + n + "->" + d)
        visited += n -> d
        if ( d < visited(best)) {
          println("new best:" + (n, c) + "->" + d)
          best = n
        }
      }
      //println(queue)
    }
    println("num keys searched: " + visited.size)
    println("best: " + (best, visited(best)))
    return new Vigenere(best).decrypt(cipherText)
  }
}

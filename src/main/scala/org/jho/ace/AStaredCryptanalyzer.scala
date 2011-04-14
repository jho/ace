/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Configuration
import org.jho.ace.util.Language
import org.jho.ace.CipherText._
import org.jho.ace.Keyword._

import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet
import scala.collection.mutable.PriorityQueue
import scala.math._

class AStaredCryptanalyzer extends Cryptanalyzer with Configuration {

  def decrypt(cipherText:String)(implicit language:Language):String = {
    var queue = new PriorityQueue[(String, Double)]()
    var visited = new HashMap[String, Double]()
    var baseLine = computeBaseline(cipherText)
    println("base line: " + baseLine)
    def cost(key:String):Double = {
      val decryption = new Vigenere(key).decrypt(cipherText)
      Configuration.heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(decryption)}
    }
    var key = language.frequencies.head._1.toString
    println("starting key: " + key)
    var best = (key, cost(key))
    queue += best
    visited += best._1 -> 0
    var max = pow(language.alphabet.size, cipherText.size)/2 //search no more than half the keyspace
    while(abs(baseLine - best._2) > .1 && !queue.isEmpty && visited.size <= max) {
      var next = queue.dequeue
      next._1.neighbors(true).withFilter(!visited.contains(_)).map(n => (n, cost(n))).foreach { n =>
        //there are no loops in the "graph;, so we can just add all the neighbors t the queue
        var dist = visited(next._1) + n._2
        //println("checking: " + n._1 + "->" + dist)
        if ( dist < visited(best._1) ) {
          best = n
          //println("new best:" + best + "->" + dist)
          queue += n
        }
        visited += n._1 -> dist
      }
    }
    println("num keys searched: " + visited.size)
    println(best)
    return new Vigenere(best._1).decrypt(cipherText)
  }

  /**
   *  Compute a baseline cost for a series of plaintexts in the
   *  given language that are the same length as the cipherText
   */
  def computeBaseline(cipherText:String):Double = {
    var sum = (1 to 100).foldLeft(0.0) { (acc, w) =>
      var sample = language.sample(cipherText.size)
      acc + Configuration.heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(sample)}
    }
    sum/100
  }

  //order keys by lowest cost
  implicit def orderedKeyCostTuple(f: (String, Double)):Ordered[(String, Double)] = new Ordered[(String, Double)] {
    def compare(other: (String, Double)) = other._2.compare(f._2)
  }
}

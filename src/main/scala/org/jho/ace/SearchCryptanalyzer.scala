/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Configuration
import org.jho.ace.util.Language
import org.jho.ace.CipherText._
import org.jho.ace.Keyword._

import scala.collection.mutable.HashSet
import scala.math._

class SearchCryptanalyzer extends Cryptanalyzer with Configuration {
  var visited = new HashSet[String]()

  def decrypt(cipherText:String)(implicit language:Language):String = {
    var baseLine = computeBaseline(cipherText)
    println("base line: " + baseLine)
    def cost(key:String):Double = {
      val decryption = new Vigenere(key).decrypt(cipherText)
      Configuration.heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(decryption)}
    }
    val keyLength = cipherText.keyLengths.head
    var key = ("" /: (1 to keyLength)) { (s, i) => s + language.frequencies.head }
    println("starting key: " + key)
    var best = (key, cost(key))
    var i = 0;
    while(i <= 10000 && abs(baseLine - best._2) > .5) {
      var mutation = best._1.mutate
      if (!visited.contains(mutation)) {
        visited + mutation
        var next = (mutation, cost(mutation))
        if ( next._2 < best._2) {
          best = next
          println("new best:" + best)
        }
        i += 1
      }
    }
    println("num iterations required: " + i)
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

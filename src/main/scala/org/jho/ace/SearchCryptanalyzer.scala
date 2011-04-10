/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Configuration
import org.jho.ace.util.Language
import org.jho.ace.CipherText._
import org.jho.ace.Keyword._
import scala.collection.mutable.PriorityQueue

class SearchCryptanalyzer extends Cryptanalyzer with Configuration {
  var queue = new PriorityQueue[(String, Double)]()

  def decrypt(cipherText:String)(implicit language:Language):String = {
    def cost(key:String):Double = {
      var decryption = new Vigenere(key).decrypt(cipherText)
      Configuration.heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(decryption)}
    }
    var keyLength = cipherText.keyLengths.head
    var key = language.randomString(keyLength)
    var best = (key, cost(key, cipherText))
    queue += best
    var i = 0;
    while(!queue.isEmpty && i <= 100) {
      println(queue)
      var next = queue.dequeue
      println(next)
      if ( next._2 < best._2)
        best = next
      else
        queue ++= next._1.neighbors.map{ n =>
          (n, cost(key, cipherText))
        }
      i += 1
    }
    return null
  }

  //order keys by lowest cost
  implicit def orderedKeyCostTuple(f: (String, Double)) = new Ordered[(String, Double)] {
    def compare(other: (String, Double)) = other._2.compare(f._2)
  }
}

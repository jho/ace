/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Language
import org.jho.ace.util.Configuration
import org.jho.ace.util.Util._

import scala.math._

class CipherText(var text:String) extends FrequencyAnalyzer with Configuration {
  text = text.filter(_.isLetter).toUpperCase

  lazy val periods:Map[Int, Double] = {
    val columns = text.zipWithIndex
    (1 to text.size-1).map { i =>
      var sum = columns.groupBy(_._2 % i).foldLeft(0.0) { (sum,e) =>
        sum + IndexOfCoincidence(e._2.map(_._1))
      }
      (i, sum/i)
    }.toMap
  }

  def keyLengths():List[Int] = {
    val sorted = periods.toList.sortWith { (a,b) =>
      //sort by diff from langauge IoC, order key lengths that are close to the IoC by key length (so we try smallest first)
      ((a._1 < b._1) && (abs(language.ioc-a._2) + abs(language.ioc-b._2)) <= .5) || abs(language.ioc-a._2) < abs(language.ioc-b._2)
    }
    sorted.map(_._1).toList //TODO: check to see that the next elements in the list are congruent to the first (5,10,15, etc)
  }

  /**
   * Assuming this ciphertext has been encrypted using a polyalphabetic cipher, find the frequency distribution of each column
   */
  def columnFrequencies(keyLength:Int) = {
    import CipherText._
    text.view.zipWithIndex.groupBy(_._2 % keyLength).map { e =>
      var column = e._2.map(_._1).mkString
      (e._1, language.alphabet.map { i =>
          var decryption = column.map(c => language.int2Char((language.char2Int(c) - language.char2Int(i)) mod 26))
          var freq = decryption.frequencies
          var corr = decryption.foldLeft(0.0) { (corr, c) =>
            corr + (freq(c) * language.frequencies(c))
          }
          (i, corr)
        }.sortWith(_._2 > _._2))
    }
  }
}

object CipherText {
  implicit def charSeq2CipherText(seq:Seq[Char]):CipherText = new CipherText(seq.mkString)
  implicit def string2CipherText(text:String):CipherText = new CipherText(text)
}
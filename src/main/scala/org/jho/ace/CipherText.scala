/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Language
import org.jho.ace.util.Configuration

import scala.math._

class CipherText(var text:String) extends FrequencyAnalyzer with Configuration {
  text = text.filter(_.isLetter).toUpperCase

  def periods:Map[Int, Double] = {
    var columns = text.zipWithIndex
    (1 to text.size-1).map { i =>
      var sum = columns.groupBy(_._2 % i).foldLeft(0.0) { (sum,e) =>
        sum + IndexOfCoincidence(e._2.map(_._1))
      }
      (i, sum/i)
    }.toMap
  }

  def keyLengths()(implicit language:Language):List[Int] = {
    println(periods)
    var sorted = periods.toList.sortWith { (a,b) =>
      abs(language.ioc-a._2) < abs(language.ioc-b._2)
    }
    sorted.map(_._1).toList //TODO: check to see that the next elements in the list are congruent to the first (5,10,15, etc)
  }
}

object CipherText {
  implicit def charSeq2CipherText(seq:Seq[Char]):CipherText = new CipherText(seq.mkString)
  implicit def string2CipherText(text:String):CipherText = new CipherText(text)
}

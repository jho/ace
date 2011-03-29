/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Util._
import scala.collection.Map
import scala.collection.mutable.HashMap

class IndexOfCoincidence(text:Seq[Char]) {
  def computeIoC():Double = {
    compute(text)
  }

  def testPeriod:Map[Int, Double] = {
    var periods = new HashMap[Int, Double]
    var columns = text.zipWithIndex
    //TODO: where to actually stop
    //what if the key length were almost as long as the text
    for ( i <- 1 to text.length/2) {
      periods.put(i, compute(columns.withFilter(_._2 % i == 0).map(_._1)))
    }
    return periods
  }

  private def compute(chars:Seq[Char]):Double = {
    var counts = chars.filter(_ != ' ').groupBy(_.toUpper).mapValues(_.size)
    var sum = counts.foldLeft(0.0) {
      case (sum, (key, value)) => {
          sum + (value * (value - 1));
        }
    }
    return sum/((chars.size*(chars.size-1))/26)
  }
}

object IndexOfCoincidence {
  implicit def charSeq2IC(seq:Seq[Char]):IndexOfCoincidence = new IndexOfCoincidence(seq)
  implicit def string2IC(text:String):IndexOfCoincidence = new IndexOfCoincidence(text)
}
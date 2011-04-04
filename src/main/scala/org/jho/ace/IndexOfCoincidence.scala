/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Util._
import scala.math._

class IndexOfCoincidence(var text:Seq[Char]) {
  text = text.filter(_.isLetter).map(_.toUpper) 

  def computeIoC():Double = {
    compute(text)
  }

  def testPeriod:Map[Int, Double] = {
    var columns = text.zipWithIndex
    (1 to text.size/2).map { i =>
        var sum = columns.groupBy(_._2 % i).foldLeft(0.0) { (sum,e) =>
          sum + compute(e._2.map(_._1))
        }
        (i, sum/i) 
    }.toMap
  }

  def findKeyLength:Int = {
    var sorted = testPeriod.toList.sortWith { (a,b) =>
      abs(1.73-a._2) < abs(1.73-b._2) && a._1 < b._1
    }
    sorted.head._1 //TODO: check to see that the next elements in the list are congruent to the first (5,10,15, etc)
  }

  private def compute(chars:Seq[Char]):Double = {
    var counts = chars.groupBy(identity).mapValues(_.size)
    //will get NaN if all characters have count == 1
    if (counts.filterNot(_._2 > 1).size <= 1)
      return 0.0
    var sum = counts.foldLeft(0.0) {
      case (sum, (k, v)) => {
          sum + (v * (v - 1));
        }
    }
    if ( sum > 0.0)
      return (sum/(chars.size*(chars.size-1)))/(1/26.0)
    else
      return 0.0
  }
}

object IndexOfCoincidence {
  implicit def charSeq2IC(seq:Seq[Char]):IndexOfCoincidence = new IndexOfCoincidence(seq)
  implicit def string2IC(text:String):IndexOfCoincidence = new IndexOfCoincidence(text)
}
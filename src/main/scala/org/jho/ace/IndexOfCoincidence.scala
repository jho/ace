/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Util._
import scala.collection.Map
import scala.math._

class IndexOfCoincidence(var text:Seq[Char]) {
  text = text.filter(_.isLetter).map(_.toUpper) 

  def computeIoC():Double = {
    compute(text)
  }

  def testPeriod:Map[Int, Double] = {
    var columns = text.zipWithIndex
    //collect the IoC for each "column" of text based on key sizes up to 1/4 the length of the text
    (1 to text.size/4).map { i =>
      (i, compute(columns.withFilter(_._2 % i == 0).map(_._1)))
      }.toMap
  }

  def findKeyLength:Int = {
    var periods = testPeriod
    //collect everything into classes of key lengths (5,10,15, etc)
    var classes = periods.map{ case(k,v) =>
        (k, periods.withFilter { e =>
            println(e); e._1 > k && e._1 % k == 0
          } map(_._2))
    }
    //get the average for each class
    var avgs = classes.mapValues{ v => (v.foldLeft(0.0)(_+_)/v.size) }
    //sort the lists and attemp to decrypt
    var sorted = periods.toList.sortWith { (a,b) =>
      abs(1.73-a._2) < abs(1.73-b._2)
    }
    println(sorted)
    return 1;
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
      return sum/((chars.size*(chars.size-1))/26.0)
    else
      return 0.0
  }
}

object IndexOfCoincidence {
  implicit def charSeq2IC(seq:Seq[Char]):IndexOfCoincidence = new IndexOfCoincidence(seq)
  implicit def string2IC(text:String):IndexOfCoincidence = new IndexOfCoincidence(text)
}
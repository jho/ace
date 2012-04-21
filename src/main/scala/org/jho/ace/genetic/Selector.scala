/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.genetic

import scala.util.Random

import org.jho.ace.util.LogHelper
import org.jho.ace.util._

trait Selector extends Function2[List[(String, Double)], Int, List[String]] {
  protected var rand = new Random
}

class randomSelector extends Selector with LogHelper {
  def apply(parents:List[(String, Double)], size:Int):List[String] = { 
    var indicies = size.times { rand.nextInt(parents.size) }.toList
    indicies.map(i => parents(i)._1)
  }
}

class tournamentSelector(var n:Int) extends Selector with LogHelper {
  def apply(parents:List[(String, Double)], size:Int):List[String] = { 
    return size.times { selectOne(parents) }.toList
  }

  def selectOne(parents:List[(String, Double)]):String = {
    var best = ("", Double.MaxValue) 

    var size = parents.size
    for(i <- 0 to n) {
      var next = parents(rand.nextInt(parents.size))
      if(next._2 < best._2) {
        best = next
      }
    }
    return best._1
  }
}


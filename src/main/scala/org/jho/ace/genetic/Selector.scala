/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.genetic

import scala.util.Random

import org.jho.ace.util.LogHelper

trait Crossover extends Function2[String, String, (String, String)] {
  protected var rand = new Random
}

class twoPointCrossover extends Crossover with LogHelper {
  def apply(x: String, y: String):(String, String) = { 
    var length = x.length 
    if(x.length > y.length) length = y.length 
    //get the first point
    val p1 = rand.nextInt(length)
    //get the second point which must be greater than the first
    val p2 = p1+rand.nextInt(length-p1)
    logger.debug("points = "+ (p1, p2))
    //children ar created by exchanging the parts of x and y that lie between p1 and p2
    val c1 = x.patch(p1, y.slice(p1, p2), p2-p1)
    val c2 = y.patch(p1, x.slice(p1, p2), p2-p1)
    logger.debug((x, y) + " => " + (c1, c2))
    (c1, c2)
  }
}

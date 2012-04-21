/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Configureable
import org.jho.ace.util._

import scala.util.Random
import scala.collection.mutable.ListBuffer
import scala.math._

class Keyword(var text:String) extends Configureable {
  text = text.filter(_.isLetter).toUpperCase
  val rand = new Random();

  def neighbors(grow:Boolean, shrink:Boolean):List[String] = {
    var result = (for(i <- 0 until text.size; j <- language.alphabet) yield {
        var res = ListBuffer[String]()
        if(text(i) != j) {
          res += text.updated(i, j)
        }
        if(grow) {
          res += text + j
          res += j + text
        }
        res
      }).flatten
    if(text.size > 1) {
      result = result ++ (for ( i <- 0 until text.size; j <- language.alphabet) yield {
          text.take(i) + j + text.drop(i)
        }).toList
    }
    //convert to set to remove duplicates
    result.toSet.toList
  }

  def neighbors:List[String] = {
    neighbors(false, false)
  }
 
  def mutate(growOrShrink:Boolean):String = {
    var char = language.alphabet(rand.nextInt(language.alphabet.size))
    var idx = rand.nextInt(text.size)
    if ( growOrShrink && .5 <= rand.nextDouble()) {
      if(rand.nextInt(2) == 0) { //grow or...
        if(rand.nextInt(2) == 0) {
          text + char //grow to right
        } else { 
          char + text //grow to left
        }
      } else if (text.size > 1) { //shrink
        text.take(idx) + text.drop(idx+1)
      } else {
        text.updated(idx, char)
      }
    }
    else
      text.updated(idx, char)
  }

  //can't use default parameter when there is an implicit param specified
  def mutate():String = {
    mutate(false)
  }
}

object Keyword {
  implicit def charSeq2Keyword(seq:Seq[Char]):Keyword = new Keyword(seq.mkString)
  implicit def string2Keyword(text:String):Keyword = new Keyword(text)
}

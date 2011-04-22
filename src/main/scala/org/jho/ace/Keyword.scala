/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Language
import org.jho.ace.util.Util._

import scala.collection.mutable.HashSet
import scala.util.Random
import scala.math._

class Keyword(var text:String) {
  text = text.filter(_.isLetter).toUpperCase
  val rand = new Random();

  def neighbors(grow:Boolean, shrink:Boolean)(implicit language:Language):List[String] = {
    var result = (for ( i <- 0 until text.size; j <- language.alphabet.withFilter(_!=text(i)) ) yield {
        text.updated(i, j)
      }).toList
    if ( grow )
      result = result ::: (for ( j <- language.alphabet ) yield { text + j }).toList
    if ( shrink && text.size > 1)
      result = result ::: (for ( j <- 0 until text.size ) yield { text.take(j) + text.drop(j+1) }).toList
    result
  }

  //can't use default parameter when there is an implicit param specified
  def neighbors(implicit language:Language):List[String] = {
    neighbors(false, false)
  }
 
  def mutate(growOrShrink:Boolean)(implicit language:Language):String = {
    var char = language.alphabet(rand.nextInt(language.alphabet.size))
    var idx = 0
    if ( growOrShrink )
      idx = rand.nextInt(text.size+1)
    else
      idx = rand.nextInt(text.size)
    if ( idx > text.size-1)
      text + char
    else {
      if ( !growOrShrink || (rand.nextInt(2) == 0 || text.size == 1) )
        text.updated(idx, char)
      else
        text.take(idx) + text.drop(idx+1)
    }
  }

  //can't use default parameter when there is an implicit param specified
  def mutate()(implicit language:Language):String = {
    mutate(false)
  }
}

object Keyword {
  implicit def charSeq2Keyword(seq:Seq[Char]):Keyword = new Keyword(seq.mkString)
  implicit def string2Keyword(text:String):Keyword = new Keyword(text)
}

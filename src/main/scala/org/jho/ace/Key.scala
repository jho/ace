/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace

trait Key {
  def neighbors(grow:Boolean=true, shrink:Boolean=true):List[Key]
  def mutate(growOrShrink:Boolean=true):Key
}

object Key {
  implicit def charSeq2Keyword(seq:Seq[Char]):Keyword = new Keyword(seq.mkString)
  implicit def string2Keyword(text:String):Keyword = new Keyword(text)
  implicit def keyword2String(keyword:Keyword):String = keyword.text
}

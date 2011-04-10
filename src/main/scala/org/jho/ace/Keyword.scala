/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Language

class Keyword(var text:String) {
  def neighbors(implicit language:Language):List[String] = {
    var ti = text.zipWithIndex
    (for ( i <- 0 until text.size ) yield {
        ti.map { e => if ( e._2 == i ) language.randomChar else e._1 }.mkString
      }).toList
  }
}

object Keyword {
  implicit def charSeq2Keyword(seq:Seq[Char]):Keyword = new Keyword(seq.mkString)
  implicit def string2Keyword(text:String):Keyword = new Keyword(text)
}

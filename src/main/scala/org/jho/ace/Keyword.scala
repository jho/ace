/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Language
import scala.collection.mutable.HashSet

class Keyword(var text:String) {
  def neighbors(implicit language:Language):List[String] = {
    var ti = text.zipWithIndex
    (for ( i <- 0 until text.size ) yield {
        ti.map { e => if ( e._2 == i ) language.randomChar else e._1 }.mkString
      }).toList
  }

  def permutations():Seq[String] = {
    var res = new HashSet[String]
    var p = nextPermutation(text)
    while ( !res.contains(p) ) {
      res += p
      p = nextPermutation(p)
    }
    res.toSeq
  }

  private def nextPermutation(n:String):String = {
    val pivot = n.zip(n.tail).lastIndexWhere{ case (first, second) => first < second }
    if (pivot < 0) return n.reverse
    val successor = n.lastIndexWhere{_ > n(pivot)}
    return (n.take(pivot) :+ n(successor)) +
    ((n.slice(pivot+1, successor):+ n(pivot)) + n.drop(successor+1)).reverse
  }
}

object Keyword {
  implicit def charSeq2Keyword(seq:Seq[Char]):Keyword = new Keyword(seq.mkString)
  implicit def string2Keyword(text:String):Keyword = new Keyword(text)
}

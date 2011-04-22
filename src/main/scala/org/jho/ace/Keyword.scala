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

  def neighbors(grow:Boolean)(implicit language:Language):List[String] = {
    var result = (for ( i <- 0 until text.size; j <- language.alphabet.withFilter(_!=text(i)) ) yield {
        text.updated(i, j)
      }).toList
    if ( grow )
      result = result ::: (for ( j <- language.alphabet ) yield { text + j }).toList
    result
  }

  //can't use default parameter when there is an implicit param specified
  def neighbors(implicit language:Language):List[String] = {
    neighbors(false)
  }
 
  def sizeOfNeighborhood(implicit language:Language):BigInt = {
    pow(language.alphabet.size, text.size).toInt
  }

  def mutate(grow:Boolean)(implicit language:Language):String = {
    var char = language.alphabet(rand.nextInt(language.alphabet.size))
    var idx = 0
    if ( grow )
      idx = rand.nextInt(text.size+1)
    else
      idx = rand.nextInt(text.size)
    if ( idx > text.size-1)
      text + char
    else
      text.updated(idx, char)
  }

  //can't use default parameter when there is an implicit param specified
  def mutate()(implicit language:Language):String = {
    mutate(false)
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

  def nextPermutation:String = nextPermutation(text)

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

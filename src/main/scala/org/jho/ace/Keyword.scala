/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Language
import org.jho.ace.util.Util._

import scala.collection.mutable.HashSet
import scala.util.Random

class Keyword(var text:String) {
  text = text.filter(_.isLetter).toUpperCase
  val rand = new Random();
  
  def sizeOfNeighborhood(implicit language:Language):Int = {
      (language.alphabet.size!) / ((5!) * ((language.alphabet.size!)-5)!)
  }

  def mutate(implicit language:Language):String = {
    //either swap out a random character in the keyword
    //or add a char to the keyword
    val idx = rand.nextInt(text.size) 
    val char = language.alphabet(rand.nextInt(language.alphabet.size))
    if ( idx < text.size)
      text.updated(idx, char)
    else
      text + char
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

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

import java.util.Locale

import scala.util.Random

trait Language {
  //properties
  var rand = new Random(System.currentTimeMillis);
  var locale:Locale
  var alphabet:List[Char]
  lazy val numChars = alphabet.size
  var dictionary = new Dictionary(locale)

  private lazy val sampleText = {
    scala.io.Source.fromInputStream(getClass.getResourceAsStream(List("/sample", locale.getLanguage, locale.getCountry).mkString("_"))).toIndexedSeq
  }

  def sample(size:Int):String = {
    var text = new StringBuilder
    //take 100 characters (A to Z, no spaces or punctuation) from a random point in the file
    sampleText.drop(rand.nextInt(sampleText.size/2)).takeWhile{ c =>
      if ( c.isLetter ) text + c.toUpper
      text.size < size
    }
    text.toString
  }

  //statistics
  var frequencies:Map[Char,Double]
  var bigramFrequencies:Map[String,Double]
  var trigramFrequencies:Map[String,Double]
  var ioc:Double

  //utilities
  def char2Int(c:Char):Int = alphabet.indexOf(c)
  def int2Char(i:Int):Char = alphabet(i)
  def randomChar:Char = alphabet(rand.nextInt(alphabet.size-1))
}

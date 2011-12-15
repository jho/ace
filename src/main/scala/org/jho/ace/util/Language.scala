/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

import java.util.Locale

import scala.util.Random

trait Language {
  //properties
  val rand = new Random();
  val locale:Locale
  val alphabet:List[Char]
  lazy val numChars = alphabet.size
  val dictionary = new Dictionary(locale)

  private lazy val sampleText = {
    var sample:Seq[Char] = List[Char]()
    for(i <- 1 to 3) {
      sample = sample ++ scala.io.Source.fromInputStream(
        getClass.getResourceAsStream(List("/sample"+i, locale.getLanguage, locale.getCountry).mkString("_")))
      .toIndexedSeq.withFilter(_.isLetter).map(_.toUpper)
    }
    sample
  }

  def sample(size:Int = -1):Seq[Char] = {
    val text = new StringBuilder
    if(size == -1) 
      return sampleText
    else {
      val start = rand.nextInt(sampleText.size-size)
      return sampleText.drop(start).take(size)
    }
  }

  //statistics
  val frequencies:Map[Char,Double]
  val bigramFrequencies:Map[String,Double]
  val trigramFrequencies:Map[String,Double]
  val ioc:Double
  val avgWordSize:Int

  //utilities
  def char2Int(c:Char):Int = alphabet.indexOf(c)
  def int2Char(i:Int):Char = alphabet(i)
  def randomChar:Char = alphabet(rand.nextInt(alphabet.size-1))
  def randomString(size:Int):String = (0 until size).foldLeft(""){(acc, i) => acc + randomChar}
}
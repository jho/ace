/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

import java.util.Locale
import java.io.ObjectInputStream

import scala.util.DynamicVariable
import scala.util.Random

trait Language {
  //properties
  val rand = new Random();
  val locale:Locale
  val alphabet:List[Char]
  lazy val numChars = alphabet.size
  val dictionary = new Dictionary(locale)
  val stopwords:List[String]

  lazy val sampleText = {
    var sample:Seq[Char] = List[Char]()
    for(i <- 1 to 1) {
      sample = sample ++ scala.io.Source.fromInputStream(
        getClass.getResourceAsStream(List("/sample"+i, locale.getLanguage, locale.getCountry).mkString("_")))
      .toIndexedSeq.withFilter(_.isLetter).map(_.toUpper)
    }
    sample
  }

  lazy val text = {
    var text:Seq[Char] = List[Char]()
    for(i <- 1 to 1) {
      text = text ++ scala.io.Source.fromInputStream(
        getClass.getResourceAsStream(List("/sample"+i, locale.getLanguage, locale.getCountry).mkString("_")))
      .toIndexedSeq.withFilter(c => c.isLetter || c.isWhitespace).map(_.toUpper)
    }
    text
  }

  def sample(size:Int):String = {
    sampleText.drop(rand.nextInt(sampleText.size-size)).take(size).mkString
  }

  private def loadGramFreq(file:String):Map[String, Double] = {
    var is = getClass.getResourceAsStream(List("/" +file, locale.getLanguage, locale.getCountry).mkString("_"))
    val in = new ObjectInputStream(is)
    val obj = in.readObject()
    obj match {
      case f: Seq[Tuple2[String, Double]] => f.toMap
      case _ => throw new IllegalStateException("Gram file: " + file + " is not in the right format!")
    }
    
  }

  //statistics
  val frequencies:Map[Char,Double]
  lazy val bigramFrequencies:Map[String,Double] = {
    loadGramFreq("2_grams")
  }

  lazy val trigramFrequencies:Map[String,Double] = {
    loadGramFreq("3_grams")
  } 
  lazy val fourgramFrequencies:Map[String,Double] = {
    loadGramFreq("4_grams")
  } 
  lazy val wordFrequencies:Map[String, Double] = {
    loadGramFreq("word_frequencies")
  }

  val ioc:Double
  val avgWordSize:Int

//utilities
  def char2Int(c:Char):Int = alphabet.indexOf(c)
  def int2Char(i:Int):Char = alphabet(i)
  def randomChar:Char = alphabet(rand.nextInt(alphabet.size-1))
  def randomString(size:Int):String = (0 until size).foldLeft(""){(acc, i) => acc + randomChar}
}

object Language {
  //make the default language a thread local variable
  private var _default = new DynamicVariable[Language](new English)
  def default = { _default.value }
  def default_= (language:Language) = _default.value = language
  /*
   def withDefault(language:Language)(thunk: => Language):Language = {
   _default.withValue(language)(thunk)
   }*/
}
/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.tools

import org.jho.ace.util.Language

trait FrequencyAnalyzer {
  var text:String

  lazy val frequencies:Map[Char, Double] = {
    text.groupBy(identity).mapValues(_.size/(text.size*1.0))
  }

  lazy val bigrams:List[String] = {
    ngrams(3)
  }

  lazy val bigramFrequencies:Map[String, Double] = {
    ngramFrequencies(2)
  }

  lazy val trigrams:List[String] = {
    ngrams(3)
  }

  lazy val trigramFrequencies:Map[String, Double] = {
    ngramFrequencies(3)
  }

  def ngrams(n:Int):List[String] = {
    text.grouped(n).toList
  }

  def ngramFrequencies(n:Int):Map[String, Double] = {
    var grams = ngrams(n).groupBy(identity)
    grams.map{case (k, v) => (k, (v.size*1.0)/grams.size)}.seq
  }

  lazy val words:Seq[String] = {
    var size = text.size
    if(size > 10) size = 10
    (2 to 10).flatMap { k => 
      text.sliding(k).withFilter(Language.default.dictionary.wordsUpperCase.contains(_)).toSet
    }
  }
}

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.tools

trait FrequencyAnalyzer {
  var text:String

  lazy val frequencies:Map[Char, Double] = {
    text.groupBy(identity).mapValues(_.size/(text.size*1.0))
  }

  lazy val bigramFrequencies:Map[String, Double] = {
    ngramFrequencies(2)
  }

  lazy val trigramFrequencies:Map[String, Double] = {
    ngramFrequencies(3)
  }

  def ngramFrequencies(n:Int):Map[String, Double] = {
    var grams = text.sliding(n).toList.groupBy(identity)
    //var freq = 
    grams.map{case (k, v) => (k, (v.size*1.0)/grams.size)}//.toList.sortWith(_._2 > _._2)
    //println(freq)
    //freq.toMap
  }
}

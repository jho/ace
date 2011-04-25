/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

trait FrequencyAnalyzer {
  var text:String

  def frequencies:Map[Char, Double] = {
    text.groupBy(identity).mapValues(_.size/(text.size*1.0))
  }

  def bigramFrequencies:Map[String, Double] = {
    ngramFrequencies(2)
  }

  def trigramFrequencies:Map[String, Double] = {
    ngramFrequencies(3)
  }

  def ngramFrequencies(n:Int):Map[String, Double] = {
    text.sliding(n).toList.groupBy(identity).mapValues(_.size/(text.size/(n*1.0)))
  }
}

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

class FrequencyAnalyzer(var chars:Seq[Char]) {
    chars = chars.filter(_.isLetter).map(_.toUpper)

    def frequencies:Map[Char, Double] = {
        chars.groupBy(identity).mapValues(_.size/(chars.size*1.0))
    }

    def bigramFrequencies:Map[String, Double] = {
        ngramFrequencies(2)
    }

    def trigramFrequencies:Map[String, Double] = {
        ngramFrequencies(3)
    }

    def ngramFrequencies(n:Int):Map[String, Double] = {
        chars.sliding(n).toList.map(_.toString).groupBy(identity).mapValues(_.size/(chars.size/(n*1.0)))
    }
}

object FrequencyAnalyzer {
    implicit def charSeq2Freq(seq:Seq[Char]):FrequencyAnalyzer = new FrequencyAnalyzer(seq)
    implicit def string2Freq(text:String):FrequencyAnalyzer = new FrequencyAnalyzer(text)
}
/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

import java.util.Locale

import scala.util.Random

trait Language {
    //properties
    var rand = new Random(System.currentTimeMillis);
    def locale:Locale
    def alphabet:List[Char]
    lazy val numChars = alphabet.size
    var dictionary = new Dictionary(locale)

    //statistics
    def frequencies:Map[Char,Double]
    def bigramFrequencies:Map[String,Double]
    def trigramFrequencies:Map[String,Double]
    def ioc:Double

    //utilities
    def char2Int(c:Char):Int = alphabet.indexOf(c)
    def int2Char(i:Int):Char = alphabet(i)
    def randomChar:Char = alphabet(int2Char(rand.nextInt(alphabet.size)))
}

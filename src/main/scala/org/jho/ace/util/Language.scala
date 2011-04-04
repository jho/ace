/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

import java.util.Locale

trait Language {
    def locale:Locale
    def alphabet:List[Char]
    lazy val numChars = alphabet.size
    var dictionary = new Dictionary(locale)
    def frequencies:Map[Char,Double]
    def ioc:Double
    def char2Int(c:Char):Int = alphabet.indexOf(c)
    def int2Char(i:Int):Char = alphabet(i)
}

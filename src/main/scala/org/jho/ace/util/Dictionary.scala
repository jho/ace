/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

import java.util.Locale
import scala.util.Random

class Dictionary(var locale:Locale) {
    var rand = new Random(System.currentTimeMillis);
    lazy val words:Set[String] = {
        scala.io.Source.fromInputStream(getClass.getResourceAsStream(List("/words", locale.getLanguage, locale.getCountry).mkString("_"))).getLines.toSet
    }
    lazy val wordsUpperCase:Set[String] = words.map(_.toUpperCase)
    def randomWord(length:Int):String = {
        var word = "";
        var wordList = wordsUpperCase.toList
        while(word.size != length) {
            word = wordList(rand.nextInt(wordList.size-1))
        }
        word
    }
}

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

import java.util.Locale

class Dictionary(var locale:Locale) {
    lazy val words:Set[String] = {
        scala.io.Source.fromInputStream(getClass.getResourceAsStream(List("/words", locale.getLanguage, locale.getCountry).mkString("_"))).getLines.toSet
    }
    lazy val wordsUpperCase:Set[String] = words.map(_.toUpperCase)
}

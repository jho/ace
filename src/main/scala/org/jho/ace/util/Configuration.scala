/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

import org.jho.ace.DictionaryHeuristic
import org.jho.ace.GramHeuristic

trait Configuration {
    implicit val language = new English
}

object Configuration {
    val heuristics = List(new DictionaryHeuristic(1.0), new GramHeuristic(1.0))
}

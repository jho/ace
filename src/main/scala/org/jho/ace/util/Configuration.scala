/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

import org.jho.ace.heuristic.DictionaryHeuristic
import org.jho.ace.heuristic.GramHeuristic

trait Configuration {
    implicit val language = new English
    lazy val heuristics = List(new DictionaryHeuristic(1.0), new GramHeuristic(2.0))
}

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

import org.jho.ace.heuristic.DictionaryHeuristic
import org.jho.ace.heuristic.GramHeuristic

trait Configuration {
    implicit val language = new English
    lazy val heuristics = List(new DictionaryHeuristic(1.0), new GramHeuristic(2.0))
    /**
     * Maximum iterations a search algorithm should perform before producing it's best value
     * <p>
     * This value is based on .01% of keyspace of a 5-character keyword (or 26^5*.01)  It's a bit
     * arbitrary at the moment and is based on emperical evidence.
     */
    var maxIterations = 10000

    object SAConfig {
        var startTemp = 100.0
        var innerLoops = 50
        var outerLoops = 100
    }
}

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

import org.jho.ace.heuristic.DictionaryHeuristic
import org.jho.ace.heuristic.GramHeuristic

import scala.util.DynamicVariable

trait Configuration {
    val language = Configuration.language
    lazy val heuristics = List(new DictionaryHeuristic(1.0), new GramHeuristic(2.0))

    /**
     * Maximum iterations a search algorithm should perform before producing it's best value
     * <p>
     * This value is based on .01% of keyspace of a 5-character keyword (or 26^5*.01)  It's a bit
     * arbitrary at the moment and is based on emperical evidence.
     */
    var maxIterations = 50000

    object SAConfig {
        var startTemp = 100.0
        var innerLoops = 100
        var outerLoops = 500
        var coolingFactor = .95
    }
}

object Configuration {
    private var _language = new DynamicVariable[Language](new English)
    def language = { _language.value }
    def language_= (language:Language) = _language.value = language
    def withLanguage(language:Language)(thunk: => Language):Language = {
        _language.withValue(language)(thunk)
    }
}

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

import org.jho.ace.heuristic.DictionaryHeuristic
import org.jho.ace.heuristic.GramHeuristic

import scala.util.DynamicVariable

trait Configuration {
    val language = Configuration.language
    lazy val heuristics = List(new DictionaryHeuristic(1.0), new GramHeuristic(1.0))

    /**
     * Maximum iterations a search algorithm should perform before producing it's best value
     */
    var maxIterations = 10000

    object SAConfig {
        var startTemp = 100.0
        var innerLoops = 100
        var outerLoops = 100
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

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

import org.jho.ace.genetic.{twoPointCrossover, tournamentSelector}
import org.jho.ace.heuristic._

import scala.util.DynamicVariable

trait Configuration {
    val language = Configuration.language
    lazy val heuristics = List(new DictionaryHeuristic(3.0), new IoCHeuristic(1.5), new TrigramHeuristic(1.0))

    /**
     * Maximum iterations a search algorithm should perform before producing it's best value
     */
    var maxIterations = 10000

    object SAConfig {
        var startTemp = 100.0
        var innerLoops = 200
        var outerLoops = 500
        var coolingFactor = .97
    }

    object GAConfig {
        var population = 200
        var generations = 200
        var pm = 0.4 
        var pc = 0.9
        var selectionSize = 20 
        var crossover = new twoPointCrossover 
        var selector = new tournamentSelector(12)
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

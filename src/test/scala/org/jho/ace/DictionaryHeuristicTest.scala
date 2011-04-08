/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import org.jho.ace.util.Configuration

class DictionaryHeuristicTest extends Configuration {

    @Test
    def computeCost = {
        var result = new DictionaryHeuristic(1.0).evaluate("This is a test of the emergency broadcast system")
        println(result)
        var result2 = new DictionaryHeuristic(1.0).evaluate("blarghityhaasdfdkdkdkdkasdflkwerxapzoijwer")
        println(result2)
    }

    @Test
    def computeBaseline = {
        var sum = (0 until 100).foldLeft(0.0) { (acc, w) =>
            acc + new DictionaryHeuristic(1.0).evaluate(language.sample(100))
        }
        println(sum/100)
    }
}

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

}

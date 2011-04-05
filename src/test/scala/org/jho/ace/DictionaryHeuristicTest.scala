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
        var result = DictionaryHeuristic("This is a test")
        println(result)
    }

}

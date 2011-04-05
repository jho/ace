/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Configuration

import org.junit._
import Assert._

class GramHeuristicTest extends Configuration {

    @Test
    def computeCost = {
        var result = new GramHeuristic(1.0).evaluate("This is a test of a valid English phrase which")
        println(result)
        result = new GramHeuristic(1.0).evaluate("garbageasdfasdfasdfasdfasfadsfasdfasdfasdfasdf")
        println(result)
        result = new GramHeuristic(1.0).evaluate("garbageffffffffffffdddddddddddddddddddffffffffdddd")
        println(result)
    }

}

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
        var result = GramHeuristic("This is a test of a valid English phrase which")
        println(result)
        result = GramHeuristic("garbageasdfasdfasdfasdfasfadsfasdfasdfasdfasdf")
        println(result)
        result = GramHeuristic("garbageffffffffffffdddddddddddddddddddffffffffdddd")
        println(result)
    }

}

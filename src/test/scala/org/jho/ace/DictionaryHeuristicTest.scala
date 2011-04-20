/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import scala.math._

import org.jho.ace.util.Configuration
import org.jho.ace.util.Util._

class DictionaryHeuristicTest extends Configuration {

    @Test
    def computeCost = {
        var d = new DictionaryHeuristic(1.0)
        println(d.evaluate(("" /: (1 to 100)) { (s, i) => s + language.randomChar }))
        println(d.evaluate(("" /: (1 to 100)) { (s, i) => s + language.randomChar }))
        println(new DictionaryHeuristic(1.0).evaluate(language.sample(100)))
    }

    @Test
    def computeBaseline = {
        /*var counts = (for (i <- 1 until 100) yield {
            new DictionaryHeuristic(1.0).evaluate(language.sample(100))
          }).toList*/
        var counts = 1000.times{ new DictionaryHeuristic(1.0).evaluate(language.sample(100)) }.toList
        println("Max: " + counts.min)
        println("Min: " + counts.max)
        var avg = counts.sum/counts.size
        println("Avg: " + avg)
        //println("Std Deviation: " + avg)
        println(sqrt(counts.map(e => pow(avg - e, 2)).sum/counts.size))
    }
}

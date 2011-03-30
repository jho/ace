/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import org.jho.ace.FrequencyAnalyzer._

class FrequencyAnalyzerTest {
    @Test
    def testFrequencies = {
        var results = "This is a test".frequencies
        assertEquals(6, results.size)
        results.foreach { p => assertTrue(p._2 > 0.0) }
    }

    @Test
    def testBigramFrequencies = {
        var results = "This is a test".bigramFrequencies
        assertEquals(9, results.size)
        results.foreach { p => assertTrue(p._2 > 0.0) }
    }

    @Test
    def testNgramFrequencies = {
        var results = "This is a test".ngramFrequencies(3)
        assertEquals(9, results.size)
        results.foreach { p => assertTrue(p._2 > 0.0) }
    }
}

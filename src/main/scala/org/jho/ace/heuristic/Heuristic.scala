/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.heuristic

import org.jho.ace.CipherText
import scala.util.DynamicVariable

abstract class Heuristic(val weight:Double = 1.0) {
    def evaluate(in:CipherText):Double = {
        weight * compute(in)
    }
    protected def compute(in:CipherText):Double = { 0.0 }
}

class HeuristicList(val heuristics:List[Heuristic]) extends Heuristic {
    override def evaluate(in:CipherText):Double = {
        return heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(in)}
    }
}

object Heuristic {
    implicit def listToHeuristicList(heuristics:List[Heuristic]) = new HeuristicList(heuristics)
    //make the default heuristics a thread local variable
    private var _default = new DynamicVariable[Heuristic](List(new DictionaryHeuristic(2.0), new BigramHeuristic(4.0), new TrigramHeuristic(2.0), new IoCHeuristic(4.0)))
    def default = { _default.value }
    def default_= (heuristic:Heuristic) = _default.value = heuristic
}

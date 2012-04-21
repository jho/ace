/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.heuristic

import scala.util.DynamicVariable

abstract class Heuristic(val weight:Double = 1.0) {
    def evaluate(in:String):Double = {
        weight * compute(in)
    }
    protected def compute(in:String):Double = { 0.0 }
}

class HeuristicList(val heuristics:List[Heuristic]) extends Heuristic {
    override def evaluate(in:String):Double = {
        return heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(in)}
    }
}

object Heuristic {
    implicit def listToHeuristicList(heuristics:List[Heuristic]) = new HeuristicList(heuristics)
    //make the default heuristics a thread local variable
    private var _default = new DynamicVariable[Heuristic](
        List(new DictionaryHeuristic(1.0), new BigramHeuristic(1.0), new TrigramHeuristic(1.0)))
    def default = { _default.value }
    def default_= (heuristic:Heuristic) = _default.value = heuristic
}

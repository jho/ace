/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.heuristic

abstract class Heuristic(val weight:Double) {
  def evaluate(in:Seq[Char]):Double = {
    weight * compute(in)
  }
  protected def compute(in:Seq[Char]):Double
}

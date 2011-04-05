/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Language

abstract class Heuristic(val weight:Double) {
  def evaluate(in:String)(implicit language:Language):Double = {
    weight * compute(in)
  }
  protected def compute(in:String)(implicit language:Language):Double
}

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.heuristic

import org.jho.ace.util.Configureable
import org.jho.ace.tools.indexOfCoincidence
import scala.math._

class IoCHeuristic(weight:Double) extends Heuristic(weight) with Configureable {
  override def compute(in:String):Double = {
    val text = in.filter(_.isLetter).toUpperCase
    return abs(language.ioc - indexOfCoincidence(text))
  }
}

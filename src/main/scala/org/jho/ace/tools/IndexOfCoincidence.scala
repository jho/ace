/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.tools

import org.jho.ace.util.Configureable

object indexOfCoincidence extends Configureable {
  def apply(text:Seq[Char]):Double = {
    val counts = text.groupBy(identity).mapValues(_.size)
    //will get NaN if all characters have count == 1
    var sum = counts.foldLeft(0.0) {
      case (sum, (k, v)) => {
          sum + (v * (v - 1));
        }
    }
    if ( sum > 0.0)
      return (sum/(text.size*(text.size-1)))/(1/language.numChars.toDouble)
    else
      return 0.0
  }
}

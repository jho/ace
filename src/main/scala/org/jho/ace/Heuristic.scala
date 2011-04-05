/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Language

trait Heuristic {
  def apply(in:String)(implicit language:Language):Double
}

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Language

trait Cryptanalyzer {
  def decrypt(cipherText:String)(implicit language:Language):String
}

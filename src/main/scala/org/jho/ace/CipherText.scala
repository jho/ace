/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util._

import scala.math._

trait CipherText {
}

object CipherText {
  implicit def charSeq2StringCipherText(seq:Seq[Char]):StringCipherText = new StringCipherText(seq.mkString)
  implicit def string2StringCipherText(text:String):StringCipherText = new StringCipherText(text)
}

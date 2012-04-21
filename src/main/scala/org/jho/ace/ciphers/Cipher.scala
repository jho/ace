/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.ciphers

import org.jho.ace.util.Configureable
import org.jho.ace.heuristic._

//TODO: Make use of generics to type the ciphertext and keyword, etc
trait Cipher extends Configureable {
  def encrypt(key:String, message:String):String
  def decrypt(key:String, cipherText:String):String
  def generateInitialKey(cipherText:String, goal:Double = 0.0)(implicit heuristic:Heuristic=Heuristic.default):String 
}

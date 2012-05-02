/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.ciphers

import org.jho.ace.util.Configureable
import org.jho.ace.CipherText
import org.jho.ace.Key
import org.jho.ace.heuristic._

//TODO: Make use of generics to type the ciphertext and keyword, etc
trait Cipher[+K <: Key, +C <: CipherText] extends Configureable {
  def encrypt(key:K, message:C):C
  def decrypt(key:K, cipherText:C):C
  def generateInitialKey(cipherText:C, goal:Double = 0.0)(implicit heuristic:Heuristic=Heuristic.default):K 
}

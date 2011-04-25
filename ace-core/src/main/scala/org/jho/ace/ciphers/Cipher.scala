/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.ciphers

import org.jho.ace.util.Configuration

trait Cipher extends Configuration {
  def encrypt(key:String, message:String):String
  def decrypt(key:String, cipherText:String):String
  def generateInitialKey(cipherText:String):String 
}

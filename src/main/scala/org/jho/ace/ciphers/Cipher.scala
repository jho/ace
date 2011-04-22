/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.ciphers

import org.jho.ace.util.Language
import org.jho.ace.util.Configuration

trait Cipher extends Configuration {
  def encrypt(key:String, message:String)(implicit language:Language):String
  def decrypt(key:String, cipherText:String)(implicit language:Language):String
  def generateInitialKey(cipherText:String)(implicit language:Language):String 
}

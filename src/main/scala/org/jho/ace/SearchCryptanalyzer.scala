/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Configuration
import org.jho.ace.util.Language

class SearchCryptanalyzer extends Cryptanalyzer with Configuration {
  def decrypt(cipherText:String)(implicit language:Language):String = {
    var key = "" + language.randomChar
    var decryption = new Vigenere(key).decrypt(cipherText)
    var cost = Configuration.heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(decryption)}
    return null
  }
}

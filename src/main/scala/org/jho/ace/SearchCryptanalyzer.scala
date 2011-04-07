/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Configuration
import org.jho.ace.util.Language

class SearchCryptanalyzer extends Cryptanalyzer with Configuration {
  def decrypt(cipherText:String)(implicit language:Language):String = {
    //var key = "" + language.randomChar
    return null
  }
}

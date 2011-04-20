/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Configuration

class SearchCryptanalyzerTest extends Configuration {

  @Test
  def decrypt = {
    var ca = new SearchCryptanalyzer
    var result = ca.decrypt("QPWKALVRXCQZIKGRBPFAEOMFLJMSDZVDHXCXJYEBIMTRQWNMEAIZRVKCVKVLXNEICFZPZCZZHKMLVZVZIZRRQWDKECHOSNYXXLSPMYKVQXJTDCIOMEEXDQVSRXLRLKZHOV")
    println(result)
    assertEquals("MUSTCHANGEMEETINGLOCATIONFROMBRIDGETOUNDERPASSSINCEENEMYAGENTSAREBELIEVEDTOHAVEBEENASSIGNEDTOWATCHBRIDGESTOPMEETINGTIMEUNCHANGEDXX", result)

        /*
    var sample = new Vigenere("KEY").encrypt(language.sample(100))
    println(sample)
    result = ca.decrypt(sample)
    println(result)*/

    result = ca.decrypt("LXFOPVEFRNHR")
    println(result)
  }

}

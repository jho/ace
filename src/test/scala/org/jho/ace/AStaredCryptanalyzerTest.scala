/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Configuration

class AStaredCryptanalyzerTest extends Configuration {
  @Test
  def decrypt = {
    var ca = new AStaredCryptanalyzer
    var result = ca.decrypt("QPWKALVRXCQZIKGRBPFAEOMFLJMSDZVDHXCXJYEBIMTRQWNMEAIZRVKCVKVLXNEICFZPZCZZHKMLVZVZIZRRQWDKECHOSNYXXLSPMYKVQXJTDCIOMEEXDQVSRXLRLKZHOV")
    println(result)
    assertEquals("MUSTCHANGEMEETINGLOCATIONFROMBRIDGETOUNDERPASSSINCEENEMYAGENTSAREBELIEVEDTOHAVEBEENASSIGNEDTOWATCHBRIDGESTOPMEETINGTIMEUNCHANGEDXX", result)


    var sample = language.sample(100)
    println(sample)
    var encrypted = new Vigenere("KEY").encrypt(sample)
    println(encrypted)
    result = ca.decrypt(encrypted)
    println(result)

    println(ca.decrypt("LXFOPVEFRNHR"))
  }
}

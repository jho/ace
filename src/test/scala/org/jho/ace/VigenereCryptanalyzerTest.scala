/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import org.jho.ace.util.Configuration

class VigenereCryptanalyzerTest extends Configuration {

  @Test
  def decrypt = {
    var v = new VigenereCryptanalyzer
    var result = v.decrypt("QPWKALVRXCQZIKGRBPFAEOMFLJMSDZVDHXCXJYEBIMTRQWNMEAIZRVKCVKVLXNEICFZPZCZZHKMLVZVZIZRRQWDKECHOSNYXXLSPMYKVQXJTDCIOMEEXDQVSRXLRLKZHOV")
    println(result)
    assertEquals("MUSTCHANGEMEETINGLOCATIONFROMBRIDGETOUNDERPASSSINCEENEMYAGENTSAREBELIEVEDTOHAVEBEENASSIGNEDTOWATCHBRIDGESTOPMEETINGTIMEUNCHANGEDXX", result)

    //result = v.decrypt("LXFOPVEFRNHR")
    //println(result)
  }
}

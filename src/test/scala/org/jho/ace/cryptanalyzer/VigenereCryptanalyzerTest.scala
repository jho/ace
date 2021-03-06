/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.cryptanalyzer

import org.junit._
import Assert._

import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Configureable

class VigenereCryptanalyzerTest extends Configureable {

  @Test
  def decrypt = {
     var v = new VigenereCryptanalyzer
     var result = v.decrypt("QPWKALVRXCQZIKGRBPFAEOMFLJMSDZVDHXCXJYEBIMTRQWNMEAIZRVKCVKVLXNEICFZPZCZZHKMLVZVZIZRRQWDKECHOSNYXXLSPMYKVQXJTDCIOMEEXDQVSRXLRLKZHOV", new Vigenere())
     println(result)
     assertEquals("MUSTCHANGEMEETINGLOCATIONFROMBRIDGETOUNDERPASSSINCEENEMYAGENTSAREBELIEVEDTOHAVEBEENASSIGNEDTOWATCHBRIDGESTOPMEETINGTIMEUNCHANGEDXX", result)

     println(v.decrypt("DLCNSRUORMOSTUOXQPVRWDMKARFHCRMPFVYOEJ", new Vigenere()))

    //result = v.decrypt("LXFOPVEFRNHR")
    //println(result)
  }
}

/*
 * Copyright 2011 Joshua Hollander.
 */

package org.jho.ace.ciphers

import org.jho.ace.util.Configuration

import org.junit._
import Assert._

class VigenereTest extends Configuration {
  @Test
  def encryptDecrypt = {
    val v = new Vigenere
    val m = "ATTACKATDAWN"
    val c = "LXFOPVEFRNHR"
    var result = v.encrypt("LEMON", m)
    println(result)
    assertEquals(c, result)
    result = v.decrypt("LEMON", c)
    println(result)
    assertEquals(m, result)

    //println(v.encrypt("EVERY", "MUSTCHANGEMEETINGLOCATIONFROMBRIDGETOUNDERPASSSINCEENEMYAGENTSAREBELIEVEDTOHAVEBEENASSIGNEDTOWATCHBRIDGESTOPMEETINGTIMEUNCHANGEDXX"))
    //println(v.encrypt("KEYWORD", "THEREARENOSECRETSTHATTIMEDOESNOTREVEAL"))
  }
}

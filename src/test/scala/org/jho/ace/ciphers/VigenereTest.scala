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
    val v = new Vigenere("LEMON")
    val m = "ATTACKATDAWN"
    val c = "LXFOPVEFRNHR"
    var result = v.encrypt(m)
    println(result)
    assertEquals(c, result)
    result = v.decrypt(c)
    println(result)
    assertEquals(m, result)

    println(new Vigenere("POPCO").decrypt("ECLGFXGCQHWWCIKXHWQIIQDPHGCA"));
  }
}

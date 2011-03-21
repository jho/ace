/*
 * Copyright 2011 Joshua Hollander.
 */

package org.jho.autodecrypt

import org.junit._
import Assert._

class VigenereTest {

  @Before
  def setUp: Unit = {
  }

  @After
  def tearDown: Unit = {
  }

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
  }
}

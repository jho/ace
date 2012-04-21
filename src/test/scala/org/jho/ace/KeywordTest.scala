/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import org.jho.ace.Keyword._
import org.jho.ace.util._
import org.jho.ace.util.Configureable

class KeywordTest extends Configureable {
  @Test
  def neighborHood = {
    //assertEquals(50, "aa".neighbors.size)
    //assertEquals(78, "aa".neighbors(true, true).size)
    //println("aa".neighbors(true, true).size)
    //"aa".neighbors(true, true).foreach(println(_))
    println("----")
  }

  @Test
  def mutate = {
    100.times {
      var result = "aa".mutate(true)
      println(result)
    }
  }

  /*
   @Test
   def tabula = {
   val size = language.alphabet.size
   println("  & " + language.alphabet.mkString("&") + " \\\\ \\hline")
   println(language.alphabet(0) + "&" + language.alphabet.mkString("&") + " \\\\")
   (1 to (language.alphabet.size - 1)).foreach { s => 
   var parts = language.alphabet.splitAt(s)
   println(language.alphabet(s) + "&" + parts._2.mkString("&") + "&" + parts._1.mkString("&") + " \\\\")
   }
   }*/
}

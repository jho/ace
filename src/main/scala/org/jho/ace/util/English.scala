/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

import java.util.Locale
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import math._
import org.jho.ace.util._

class English extends {
  val locale = Locale.US
  val alphabet = ('A' to 'Z').toList
  val ioc = 1.73
  val avgWordSize = 5
  val frequencies = Map(
    'E' -> .1202,
    'T' -> .0910,
    'A' -> .0812,
    'O' -> .0768,
    'I' -> .0731,
    'N' -> .0695,
    'S' -> .0628,
    'R' -> .0602,
    'H' -> .0592,
    'D' -> .0432,
    'L' -> .0398,
    'U' -> .0288,
    'C' -> .0271,
    'M' -> .0261,
    'F' -> .0230,
    'Y' -> .0211,
    'W' -> .0209,
    'G' -> .0203,
    'P' -> .0182,
    'B' -> .0149,
    'V' -> .0111,
    'K' -> .0069,
    'X' -> .0017,
    'Q' -> .0011,
    'J' -> .0010,
    'Z' -> .0007
  )
} with Language 


object English extends Configuration {
  def main(args:Array[String]) = {
    var e = new English()

    /*
    for(i <- 2 to 4) {
      var grams = e.sampleText.sliding(i).toList
      println(grams.size)
      println(grams.groupBy(identity).size)
      var frequencies = grams.par.groupBy(identity).map(e => (e._1.mkString, (e._2.size*1.0)/grams.size)).toList.seq
      val os = new FileOutputStream(new File(List(i,"grams",e.locale.getLanguage, e.locale.getCountry).mkString("_")))
      val o = new ObjectOutputStream(os)
      o.writeObject(frequencies)
      println(frequencies.sortWith(_._2 > _._2))
    }*/
  }
}

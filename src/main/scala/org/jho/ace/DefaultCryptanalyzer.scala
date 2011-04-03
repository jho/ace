/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import scala.math._
import org.jho.ace.IndexOfCoincidence._
import org.jho.ace.FrequencyAnalyzer._
import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Util._

class DefaultCryptanalyzer(var cipherText:String) {
  var char2Freq =  Map(
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
  var freq2Char = char2Freq.toList.map(_.swap).sortWith(_._1 < _._1)

  cipherText = cipherText.filter(_.isLetter).map(_.toUpper)

  def decrypt:String = {
    var keyLength = cipherText.findKeyLength
    var key = cipherText.view.zipWithIndex.groupBy(_._2 % keyLength).foldLeft("") { (key, e) =>
      println("----")
      println("col="+e._1)
      var column = e._2.map(_._1).mkString
      println(column)
      var colKey = ('A' until 'Z').map { i =>
        var decryption = column.map(c => int2Char((char2Int(c) - char2Int(i)) mod 26))
        println(decryption)
        var freq = decryption.frequencies
        var corr = decryption.foldLeft(0.0) { (corr, c) =>
          corr + (freq(c) * char2Freq(c))
        }
        println(i+"->"+corr)
        (i, corr)
      }.sortWith(_._2 > _._2).head._1
      println("colKey="+colKey)
      key + colKey
    }
    println("key="+key)
    new Vigenere(key).decrypt(cipherText)
  }
}

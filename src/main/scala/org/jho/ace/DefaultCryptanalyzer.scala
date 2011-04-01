/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import scala.math._
import org.jho.ace.IndexOfCoincidence._
import org.jho.ace.FrequencyAnalyzer._

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
    var freq2Char = char2Freq.map(e => (e._2, e._1)).toMap
    println(freq2Char)

    cipherText = cipherText.filter(_.isLetter).map(_.toUpper)

    def decrypt:String = {
        val plainText = cipherText.toBuffer
        var keyLength = cipherText.findKeyLength
        println(keyLength)
        cipherText.view.zipWithIndex.groupBy(_._2 % keyLength).foreach { e =>
            //println(e._2.mkString)
            e._2.map(_._1).frequencies.foreach { f =>
                var sorted = freq2Char.toList.sortWith((a, b) => abs(a._1 - f._2) < abs(b._1 - f._2))
                println((f._1, f._2)+"="+sorted)
                var rep = sorted.head._2
                println("rep="+rep)
                e._2.filter(_._1 == f._1).foreach { v =>
                    println((v._1, v._2)+"->"+rep)
                    plainText(v._2) = rep
                }
                println(cipherText)
                println(plainText.mkString)
            }
        }
        println(plainText.mkString)
        null;
    }
}

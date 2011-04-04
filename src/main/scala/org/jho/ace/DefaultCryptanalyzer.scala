/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import scala.math._
import org.jho.ace.IndexOfCoincidence._
import org.jho.ace.FrequencyAnalyzer._
import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Util._
import org.jho.ace.util.Dictionary

class DefaultCryptanalyzer(var cipherText:String) {
  //package up as a "language" along with numChars and an alphabet
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
    //find the frequency correlations for each column (based on keyLength columns) of the cipherText
    var colFreqs = cipherText.view.zipWithIndex.groupBy(_._2 % keyLength).map { e =>
      var column = e._2.map(_._1).mkString
      (e._1, ('A' until 'Z').map { i =>
          var decryption = column.map(c => int2Char((char2Int(c) - char2Int(i)) mod 26))
          var freq = decryption.frequencies
          var corr = decryption.foldLeft(0.0) { (corr, c) =>
            corr + (freq(c) * char2Freq(c))
          }
          (i, corr)
        }.sortWith(_._2 > _._2))
    }
    //decrypt using each of the most probable keys and determine a "count" of dictionary words
    var keys = colFreqs.foldLeft(List[(String,Double)]()) { (keys, i) =>
      keys ++ (0 until 5).map { j =>
        var key = colFreqs.foldLeft("") { (key, e) => 
          key + (if (e._1 == i._1) e._2(j)._1 else e._2.head._1)
        }
        var decryption = new Vigenere(key).decrypt(cipherText)
        //TODO: Split this out into a function
        var dictCount = (1.0/cipherText.size)*((2 to 7).foldLeft(0.0) { (sum, k) =>
            sum + (k^2 * (decryption.sliding(k).filter(Dictionary.wordsUpperCase.contains(_)).toSet.size))
          })
        (key, dictCount)
      }
    }
    //the key that generates the highest dictionary word count "wins"
    var key = keys.sortWith(_._2 > _._2).head._1
    //now decrypt using the key
    new Vigenere(key).decrypt(cipherText)
  }
}

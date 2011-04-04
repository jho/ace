/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import scala.math._
import org.jho.ace.IndexOfCoincidence._
import org.jho.ace.FrequencyAnalyzer._
import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Util._
import org.jho.ace.util.Configuration
import org.jho.ace.util.Language

class DefaultCryptanalyzer(var cipherText:String) extends Configuration {
  def decrypt()(implicit language:Language):String = {
    var keyLength = cipherText.findKeyLength
    //find the frequency correlations for each column (based on keyLength columns) of the cipherText
    var colFreqs = cipherText.view.zipWithIndex.groupBy(_._2 % keyLength).map { e =>
      var column = e._2.map(_._1).mkString
      (e._1, language.alphabet.map { i =>
          var decryption = column.map(c => language.int2Char((language.char2Int(c) - language.char2Int(i)) mod 26))
          var freq = decryption.frequencies
          var corr = decryption.foldLeft(0.0) { (corr, c) =>
            corr + (freq(c) * language.frequencies(c))
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
            sum + (k^2 * (decryption.sliding(k).filter(language.dictionary.wordsUpperCase.contains(_)).toSet.size))
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

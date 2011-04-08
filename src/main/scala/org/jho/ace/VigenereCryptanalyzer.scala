/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import scala.math._
import org.jho.ace.CipherText._
import org.jho.ace.ciphers.Vigenere
import org.jho.ace.util.Util._
import org.jho.ace.util.Configuration
import org.jho.ace.util.Language

class VigenereCryptanalyzer extends Cryptanalyzer with Configuration {
  def decrypt(cipherText:String)(implicit language:Language):String = {
    var keyLengths = cipherText.keyLengths
    var keys = keyLengths.slice(0,5).foldLeft(List[(String,Double)]()) { (keys, keyLength) =>
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
      //decrypt using each of the most probable keys and assign a cost based on a heuristic
      keys ::: colFreqs.foldLeft(List[(String,Double)]()) { (acc, i) =>
        //try different combinations of the top 5 highest frequencies in each column (to elmininate some statisitical variance)
        //this is also arbitrary, need to base on some rule/heuristic
        acc ++ (0 until 5).map { j =>
          var key = colFreqs.foldLeft("") { (key, e) =>
            key + (if (e._1 == i._1) e._2(j)._1 else e._2.head._1)
          }
          var decryption = new Vigenere(key).decrypt(cipherText)
          (key, Configuration.heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(decryption)})
        }
      }
    }
    //the key that generates the highest dictionary word count "wins"
    var key = keys.sortWith(_._2 < _._2).head._1
    //now decrypt using the key
    new Vigenere(key).decrypt(cipherText)
  }
}

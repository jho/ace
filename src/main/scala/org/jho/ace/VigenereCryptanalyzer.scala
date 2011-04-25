/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import scala.math._
import org.jho.ace.CipherText._
import org.jho.ace.ciphers.Cipher
import org.jho.ace.util.Util._
import org.jho.ace.util.Language

class VigenereCryptanalyzer extends Cryptanalyzer {
  def decrypt(cipherText:String, cipher:Cipher):String = {
    val keyLengths = cipherText.keyLengths
    val keys = keyLengths.slice(0,5).foldLeft(List[(String,Double)]()) { (keys, keyLength) =>
      //find the frequency correlations for each column (based on keyLength columns) of the cipherText
      var colFreqs = cipherText.columnFrequencies(keyLength)
      //decrypt using each of the most probable keys and assign a cost based on a heuristic
      keys ::: colFreqs.foldLeft(List[(String,Double)]()) { (acc, i) =>
        //try different combinations of the top 5 highest frequencies in each column (to elmininate some statisitical variance)
        //this is also arbitrary, need to base on some rule/heuristic
        ///TODO: This is not working correctly (it's not enumerating all the combinations)
        acc ++ (0 until 5).map { j =>
          var key = colFreqs.foldLeft("") { (key, e) =>
            key + (if (e._1 == i._1) e._2(j)._1 else e._2.head._1)
          }
          println(key)
          var decryption = cipher.decrypt(key, cipherText)
          (key, heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(decryption)})
        }
      }
    }
    //the key that generates the highest dictionary word count "wins"
    val key = keys.sortWith(_._2 < _._2).head._1
    println(key)
    //now decrypt using the key
    cipher.decrypt(key, cipherText)
  }
}

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.util.Configureable
import org.jho.ace.tools.FrequencyAnalyzer
import org.jho.ace.tools.indexOfCoincidence
import org.jho.ace.util._

import scala.math._

class CipherText(var text:String) extends FrequencyAnalyzer with Configureable with LogHelper {
  text = text.filter(_.isLetter).toUpperCase

  lazy val periods:Map[Int, Double] = {
    val columns = text.zipWithIndex
    (1 to text.size-1).map { i =>
      var sum = columns.groupBy(_._2 % i).foldLeft(0.0) { (sum,e) =>
        sum + indexOfCoincidence(e._2.map(_._1))
      }
      (i, sum/i)
    }.toMap
  }

  lazy val keyLengths:List[Int] = {
    var sorted = periods.filter(i => abs(language.ioc-i._2) <= .3).toList.sortWith { (a,b) =>
      abs(language.ioc-a._2) - abs(language.ioc-b._2) < 0
    }.sortWith { (a,b) => 
      (b._1 mod a._1) == 0 
    } 
    logger.trace(sorted.take(sorted.size min 10))
    sorted.map(_._1).toList 
  }

  /**
   * Assuming this ciphertext has been encrypted using a polyalphabetic cipher, find the frequency distribution of each column
   */
  def columnFrequencies(keyLength:Int) = {
    import CipherText._
    text.view.zipWithIndex.groupBy(_._2 % keyLength).map { e =>
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
  }
}

object CipherText {
  implicit def charSeq2CipherText(seq:Seq[Char]):CipherText = new CipherText(seq.mkString)
  implicit def string2CipherText(text:String):CipherText = new CipherText(text)
}

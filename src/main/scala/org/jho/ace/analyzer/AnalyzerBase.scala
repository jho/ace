/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.analyzer

import org.junit._
import Assert._

import org.apache.commons.lang.builder.ToStringStyle
import org.jho.ace.ciphers.Cipher
import org.jho.ace.cryptanalyzer._
import org.jho.ace.util.Configureable
import org.jho.ace.util.Plotter
import org.jho.ace.util._
import org.apache.commons.lang.builder.ToStringBuilder

abstract class AnalyzerBase extends Configureable with LogHelper {
  class Result(var keySize:Int, var textSize:Int, var keysSearched:Double, var accuracy:Double, var runTime:Double) {
    override def toString:String = {
      return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE)
    }
  }

  protected def runCryptanalyzer(size:Int, keyLength:Int, ca:Cryptanalyzer, cipher:Cipher):Result = {
    logger.debug(size+"-char, "+keyLength+"-char keyword")
    def run(plainText:String):(Int, Double, Long) = {
      var key = language.dictionary.randomWord(keyLength)
      var cipherText = cipher.encrypt(key, plainText)
      var startTime = System.currentTimeMillis
      var result = ca.decrypt(cipherText, cipher)
      (result.numKeysSearched, plainText.distance(result.plainText), (System.currentTimeMillis - startTime))
    }
    var results = 3.times {
      run(language.sample(size))
    }.toList
    //drop the worst result (to increase consistency between runs)
    //results = results.sortBy(_._2).take(results.size-1)
    logger.debug("Results for "+ca.getClass.getSimpleName+" with "+size+"-char, "+keyLength+"-char keyword: " + results)
    var sum = results.foldLeft((0, 0.0, 0L)) { (sum, res) =>
      (sum._1 + res._1, sum._2 + (1.0 - res._2), sum._3 + res._3)
    }
    results = results.sortWith(_._2 < _._2)
    new Result(keyLength, size, (sum._1/results.size), (sum._2/results.size), (sum._3/results.size))
  }

  protected def plot(title:String, xlabel:String, ylabel:String, data:Map[String, Seq[(Double, Double)]], xrange:(Double, Double) = null, yrange:(Double, Double) = null) = {
    var plotter = new Plotter(title)
    plotter.xlabel = xlabel
    plotter.ylabel = ylabel
    if(xrange != null) plotter.xrange = xrange
    if(yrange != null) plotter.yrange = yrange
    data.zipWithIndex.foreach{ case((series, results), i) => 
        //logger.debug("data: " + series + " -> " + data)
        plotter.plot(results, series.toString+"-characters", "w lp ls "+(i+1))
    }
    plotter.run
  }
}

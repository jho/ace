/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.util

import scalala.tensor.dense._
import scalala.library.Plotting._
import java.awt.Color
import org.jho.ace.CipherText._
import scalala.operators.Implicits._
import scalala.tensor.::

object Graphs extends Configuration {
  val grams = language.trigramFrequencies.keys.toList.sorted.zipWithIndex

  protected def vectorizeGrams(text:String):DenseVector[Double] = {
    var freqs = text.trigramFrequencies
    grams.map { case(gram, i) => 
        freqs.get(gram) match {
          case Some(x) => x
          case None => 0.0
        }
    }.toArray.asVector
  }

  def main(args:Array[String]) = {
    val range = (10 to 100)
    val x = grams.map(_._2).toArray
    val y = vectorizeGrams(language.sample(100))
    //val y = range.map(language.randomString(_)).map(vectorizeGrams(_)).toArray
    val s = DenseVector.ones[Double](100)

    plot.hold = true // do not create a new figure for every plot call
    plot(x, y, '.')
    plot(vectorizeGrams(language.randomString(100)), y, '.')
  }
}

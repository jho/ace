/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.classifier

import org.jho.ace.util.Configureable
import org.jho.ace.util.LogHelper
import org.jho.ace.util._

import scala.util.Random

trait DecryptionClassifier extends Configureable with LogHelper {
    def classify(text:String):Boolean

    def score(text:String):Float

    def load:Boolean

    def train:Unit
  
}

object DecryptionClassifier extends Configureable with LogHelper {
    protected val rand = new Random()

    def mimicPartialDecryption(text:String):String = {
        var n = rand.nextInt(text.size/4) max 1 
        var cols = (1 to rand.nextInt(n)).map(i => rand.nextInt(n)).toSet
        if(cols.size == 0) cols = Set(0)
        val res = new StringBuilder(text)
        for (i <- cols) {
            for (j <- i to text.size-1 by n) {
                res.update(j, language.randomChar)
            }
        }
        res.toString
    }
}

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

class CryptanalysisResult(val keyword:String, val plainText:String, val numKeysSearched:Int, val cost:Double = 0.0) {
    override def toString = "(keyword:"+keyword+",plainext:"+plainText+",keys searched:"+numKeysSearched+",cost:"+cost+")"
}

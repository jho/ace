/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace

import org.jho.ace.ciphers.Cipher
import org.jho.ace.cryptanalyzer.AStarCryptanalyzer
import org.jho.ace.cryptanalyzer.BFSCryptanalyzer
import org.jho.ace.cryptanalyzer.GeneticCryptanalyzer
import org.jho.ace.cryptanalyzer.SACryptanalyzer
import org.jho.ace.util.Configureable
import org.jho.ace.util._

class Demo extends Configureable {
  def run(args:Map[String, String]) {
    val size = args.getOrElse("size", "200").toInt
    val plaintext = language.sample(size)
    val keyword = args.getOrElse("keyword", "KEYWORD")

    val cipher:Cipher = Class.forName("org.jho.ace.ciphers."+args.getOrElse("cipher", "Vigenere"))
      .newInstance
      .asInstanceOf[Cipher]

    //TODO: using reflection with scala default args is not working well
    //this will have to do for now
    val cryptanalyzer = args.getOrElse("cryptanalyzer", "BFS") match {
        case "AStar" => new AStarCryptanalyzer
        case "BFS" => new BFSCryptanalyzer
        case "SA" => new SACryptanalyzer
        case "Genetic" => new GeneticCryptanalyzer
    }
    
    val startTime = System.currentTimeMillis
    val ciphertext = cipher.encrypt(keyword, plaintext)
    println("Cryptanalyzing using "+cryptanalyzer.getClass.getSimpleName+"...")
    val result = cryptanalyzer.decrypt(ciphertext, cipher)

    println("----------------")
    println("Result: " + result.keyword)
    println("Accuracy: " + (1-plaintext.distance(result.plainText)))
    println("Took: " + (System.currentTimeMillis - startTime)/1000 + " seconds")
  }
}

object Demo extends CommandLine {
  def main(args:Array[String]) = {
    new Demo().run(parseArgs(args))
  }
}
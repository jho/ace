/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.CipherText._
import org.jho.ace.Keyword._
import org.jho.ace.ciphers.Cipher
import org.jho.ace.genetic._
import org.jho.ace.util._

import scala.collection.mutable.HashSet
import scala.collection.mutable.PriorityQueue
import scala.math._
import scala.util.Random
import scala.collection.breakOut

/**
 * Cryptanalyzer that uses a Simulated Annealing algorithm
 */
class GeneticCryptanalyzer extends Cryptanalyzer {

  def decrypt(cipherText:String, cipher:Cipher):CryptanalysisResult = {
    var rand = new Random()
    var visited = new HashSet[String]()
    val (goal, stdDev) = computeGoal(cipherText.size)
    if ( logger.isTraceEnabled ) {
      logger.trace("goal: " + goal)
    }
    def cost(key:String):Double = {
      val decryption = cipher.decrypt(key, cipherText)
      heuristics.foldLeft(0.0) { (acc, h) => acc + h.evaluate(decryption)}
    }
    //generate an initial population of 200 individuals ranked by cost
    val rankFunction = (x:String, y:String) => cost(x) < cost(y)
    var population:PriorityQueue[(String, Double)] = (1 to GAConfig.population).map{ _ => 
        var i = language.randomString(rand.nextInt(cipherText.length/4) + 1)
        (i, cost(i))
    }(breakOut)
    for(generation <- 1 to GAConfig.generations) {
        logger.debug("generation: " + generation)
        logger.debug("population: " + population)
        //probabilistically select the strongest individuals 
        val parents = GAConfig.selector(population.toSeq, GAConfig.selectionSize)
        logger.trace("parents: " + parents)
        var children = parents.grouped(2).foldLeft(List[(String, Double)]()) { (children, p) => 
            var pair = p
            //probabilistically crossover pairs of children
            if(rand.nextDouble < GAConfig.pc) {
                logger.trace("crossover of: " + (pair))
                pair = GAConfig.crossover(pair.head, pair.last)
            } 
            children ++ pair.map{ c => 
                //probabilistically mutate children
                var m = c
                if(rand.nextDouble < GAConfig.pm) {
                    logger.trace("mutating: " + m)
                    m = m.mutate(true)
                }
                (m, cost(m))
            }
        }
        logger.trace("children: " + children)
        population = population ++ children
        //drop the lowest ranked individuals (survival of the fittest)
        population = population.dropRight(children.size) 
    }
    val best = population.head
    new CryptanalysisResult(best._1, cipher.decrypt(best._1, cipherText), visited.size, best._2)
  }
}

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.CipherText._
import org.jho.ace.Keyword._
import org.jho.ace.ciphers.Cipher
import org.jho.ace.genetic.twoPointCrossover
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
    val crossover = new twoPointCrossover
    //generate an initial population of 200 individuals ranked by cost
    val rankFunction = (x:String, y:String) => cost(x) < cost(y)
    var population:PriorityQueue[(String, Double)] = (1 to 20).map{ _ => 
        var i = language.randomString(rand.nextInt(cipherText.length/4) + 1)
        (i, cost(i))
    }(breakOut)
    for(generation <- 1 to 2) {
        logger.debug("generation: " + generation)
        logger.debug("population: " + population)
        //select the strongest individuals 
        //TODO: needs to use a selection method (tournament?)
        val parents = population.take(5) 
        logger.debug("parents: " + parents)
        var children = parents.grouped(2).foldLeft(List[(String, Double)]()) { (children, p) => 
            //crossover pairs of children
            //TODO: This should be probabilistic
            var pair:List[String] = crossover(p.head._1, p.last._1)
            children ++ pair.map{ c => 
                //mutate children
                //TODO: This should be probabilistic
                val m = c.mutate(true)
                (m, cost(m))
            }
        }
        logger.debug("children: " + children)
        population = population ++ children
        //drop the lowest ranked individuals (survival of the fittest)
        population = population.dropRight(children.size) 
    }
    val best = population.head
    new CryptanalysisResult(best._1, cipher.decrypt(best._1, cipherText), visited.size, best._2)
  }
}

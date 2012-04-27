/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.jho.ace.CipherText._
import org.jho.ace.Keyword._
import org.jho.ace.ciphers.Cipher
import org.jho.ace.genetic._
import org.jho.ace.heuristic.Heuristic
import org.jho.ace.util._

import scala.collection.mutable.HashSet
import scala.collection.mutable.PriorityQueue
import scala.math._
import scala.util.Random
import scala.collection.breakOut

/**
 * Cryptanalyzer that uses a Simulated Annealing algorithm
 */
class GeneticCryptanalyzer(heuristic:Heuristic = Heuristic.default, val config:GeneticConfig = new GeneticConfig) extends Cryptanalyzer(heuristic) {

  def decrypt(cipherText:String, cipher:Cipher):CryptanalysisResult = {
    val rand = new Random()
    val visited = new HashSet[String]()
    val (goal, stdDev) = computeGoal(cipherText.size)
    if ( logger.isTraceEnabled ) {
      logger.trace("goal: " + goal + "+/-" + 3.0*stdDev)
    }
    //generate an initial population of 200 individuals ranked by cost
    val rankFunction = (x:String, y:String) => cost(x) < cost(y)
    val length = cipher.generateInitialKey(cipherText, goal).size
    var population:PriorityQueue[(String, Double)] = (1 to config.populationSize).map{ _ => 
      var i = language.randomString(rand.nextInt(length) + 1).mutate(true)
      (i, cost(cipher.decrypt(i, cipherText)))
    }(breakOut)
    var best = population.head
    for(generation <- 1 to config.generations) {
      logger.trace("generation: " + generation)
      logger.trace("population: " + population)
      //probabilistically select the strongest individuals 
      val parents = config.selector(population.toList, config.selectionSize)
      logger.trace("parents: " + parents)

      //probabilisitcally crossover the parents
      var (toCrossover, noCrossover) = parents.partition(c => rand.nextDouble < config.pc)
      toCrossover = Random.shuffle(toCrossover)
      toCrossover = toCrossover.grouped(2).toList.flatMap(pair => config.crossover(pair.head, pair.last))
      var children:List[String] = noCrossover ++ toCrossover

      //probabilisitcally mutate the children
      var (toMutate:List[String], notMutated:List[String]) = children.partition(_ => rand.nextDouble < config.pm)
      children = notMutated ++ Random.shuffle(toMutate).map(_.mutate(true))
      logger.trace("after mutation: " + children)

      logger.trace("children: " + children)
      children.foreach(visited += _)
      population = population ++ children.map(c => (c, cost(cipher.decrypt(c, cipherText))))
      //drop the lowest ranked individuals (survival of the fittest)
      population = population.dropRight(children.size) 
      best = population.head
      /*
      if(abs(goal - best._2) <= 3.0 * stdDev) 
        return new CryptanalysisResult(best._1, cipher.decrypt(best._1, cipherText), visited.size, best._2)*/
    }
    new CryptanalysisResult(best._1, cipher.decrypt(best._1, cipherText), visited.size, best._2)
  }
}

class GeneticConfig {
  var populationSize:Int = 200 
  var generations:Int = 500
  var pm = 0.9 
  var pc = 0.6
  var selectionSize:Int = 50 
  var crossover = new twoPointCrossover
  var selector = new tournamentSelector(7)
}

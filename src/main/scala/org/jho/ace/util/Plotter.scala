/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.util

import scala.collection.mutable.ListBuffer
import scala.sys.process._

import java.io.{File, FileWriter}

class Plotter(xlabel:String, ylabel:String) extends Configuration with LogHelper {
  var files = new ListBuffer[File]
  var commands = new ListBuffer[String]
  var plots = new ListBuffer[String]
  //commands += "set term epslatex"
  commands += "set term aqua"
  commands += "set output 'graph1.eps'"
  commands += "set xlabel '"+xlabel+"'"
  commands += "set ylabel '"+ylabel+"'"

  def plot(data:Seq[(Double, Double)], opts:Map[String, String] = Map.empty) = {
    val f = File.createTempFile("graph-data", ".dat")
    files += f
    logger.debug(f.getAbsolutePath)
    val out = new FileWriter(f)
    data.foreach(pair => out.write(pair.mkString("\t")+System.getProperty("line.separator")))
    out.close
    var command = "\""+f.getAbsolutePath+"\""
    opts.get("title") match {
      case Some(title) => command += "title '"+title+"'"
      case _ => command += "title 'series-"+files.size+"'"
    } 
    if(opts.contains("with")) {
      command += "with "+opts("with")
    }
    plots += command
  }

  def run():Boolean = {
    commands += "plot " + plots.mkString(", ")
    val res = Seq("gnuplot", "-e", commands.mkString("; ")).!
    //clean up tmp files
    files.foreach(_.delete)
    if(res != 0) {
      logger.error("gnuplot failed with error code: " + res) 
      return false
    }  
    true
  }
}

object Plotter extends Configuration {
  def main(args:Array[String]) = {

    var plotter = new Plotter("x", "y")
    plotter.plot(List((1.0,2.0), (3.0, 4.0),(1.3,5.0)), Map("title" -> "foo", "with" -> "linespoints"))
    plotter.plot(List((2.3,4.1), (3.5, 1.8)), Map("title" -> "bar"))
    plotter.plot(List((3.2,3.1)))
    plotter.run()

    println("Done")
  }
}

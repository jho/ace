/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.util

import scala.collection.mutable.ListBuffer
import scala.sys.process._

import java.io.{File, FileWriter}

class Plotter(var title:String) extends Configureable with LogHelper {
  var xlabel:String = "x" 
  var ylabel:String = "y"
  var xrange:(Double, Double) = null
  var yrange:(Double, Double) = null

  private var files = new ListBuffer[File]
  private var commands = new ListBuffer[String]
  private var plots = new ListBuffer[String]
  
  def plot(data:Seq[(Double, Double)], title:String = null, opts:String = "") = {
    val f = File.createTempFile("graph-data", ".dat")
    files += f
    logger.debug(f.getAbsolutePath)
    val out = new FileWriter(f)
    data.foreach(pair => out.write(pair.mkString("\t")+System.getProperty("line.separator")))
    out.close
    var command = "\""+f.getAbsolutePath+"\""
    if(title != null) {
      command += " title '"+title+"'"
    } else {
      command += " title 'plot "+(plots.size+1)+"'"
    }
    command += " " + opts
    plots += command
  }

  def run():Boolean = {
    //commands += "set term aqua"
    commands += "set terminal pdfcairo font \"Gill Sans,7\" linewidth 2 rounded"
    commands += "set title '"+title+"'"
    commands += "set output '"+title.toLowerCase.filter(!_.isWhitespace)+".pdf'"
    commands += "set xlabel '"+xlabel+"'"
    commands += "set ylabel '"+ylabel+"'"

    //Styling (because vanila gnuplot is ugly)
    //taken from http://youinfinitesnake.blogspot.com/2011/02/attractive-scientific-plots-with.html 
   //Line style for axes
    commands += "set style line 80 lt rgb \"#808080\""
    //Line style for grid
    commands += "set style line 81 lt 0" 
    commands += "set style line 81 lt rgb \"#808080\"" 
    commands += "set grid back linestyle 81"
    commands += "set border 3 back linestyle 80"
    commands += "set xtics nomirror"
    commands += "set ytics nomirror"

    commands += "set style line 1 lt rgb \"#00A000\" lw 2 pt 6"
    commands += "set style line 2 lt rgb \"#5060D0\" lw 2 pt 4"
    commands += "set style line 3 lt rgb \"#F25900\" lw 2 pt 7"
    commands += "set style line 4 lt rgb \"#A00000\" lw 2 pt 5"
    commands += "set style line 5 lt rgb \"#FFCD00\" lw 2 pt 13"
    commands += "set style line 6 lt rgb \"#FF8900\" lw 2 pt 9"
    commands += "set style line 7 lt rgb \"#7908AA\" lw 2 pt 12"
    commands += "set style line 8 lt rgb \"#0BCF00\" lw 2 pt 10"

    commands += "set key bottom right"

    if(xrange != null) commands += "set xrange ["+xrange.mkString(":")+"]"
    if(yrange != null) commands += "set yrange ["+yrange.mkString(":")+"]"

    commands += "plot " + plots.mkString(", ")
    val out = new FileWriter(new File(title.toLowerCase.filter(!_.isWhitespace)+".plot"))
    commands.foreach(c => out.write(c+System.getProperty("line.separator")))
    out.close
    //val res = Seq("gnuplot", "-e", commands.mkString("; ")).!
    //clean up tmp files
    //files.foreach(_.delete)
    /*if(res != 0) {
      logger.error("gnuplot failed with error code: " + res) 
      return false
    }*/
    true
  }
}

object Plotter extends Configureable {
  def main(args:Array[String]) = {

    var plotter = new Plotter("set")
    plotter.xlabel = "x"
    plotter.ylabel = "y"
    plotter.plot(List((1.0,2.0), (3.0, 4.0),(1.3,5.0)), "foo", "w lp ls 6")
    plotter.plot(List((2.3,4.1), (3.5, 1.8)), "bar", "w lp ls 7")
    plotter.plot(List((3.2,3.1)), null, "w lp ls 8")
    plotter.run()

    println("Done")
  }
}

/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.util

trait CommandLine {
    def parseArgs(argv:Array[String]):Map[String, String] = {
    //seperate options from argumetns
    val (opts, args) = argv.partition{ _.startsWith("-") }

    //turning options array into map
    return Map() ++ opts.map{ x => 
        val pair = x.split("-{1,2}")(1).split("=")
        if(pair.length ==1) (pair(0), "true") else (pair(0), pair(1))
    }     

    //use the option values
    //val verbose = optsMap.getOrElse("v", "false").toBoolean
    //val number = optsMap.getOrElse("n", "0").toInt
    }
}

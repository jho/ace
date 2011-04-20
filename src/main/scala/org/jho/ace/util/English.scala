/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

import java.util.Locale

class English extends {
  val locale = Locale.US
  val alphabet = ('A' to 'Z').toList
  val ioc = 1.73
  val avgWordSize = 5
  val frequencies =  Map(
    'E' -> .1202,
    'T' -> .0910,
    'A' -> .0812,
    'O' -> .0768,
    'I' -> .0731,
    'N' -> .0695,
    'S' -> .0628,
    'R' -> .0602,
    'H' -> .0592,
    'D' -> .0432,
    'L' -> .0398,
    'U' -> .0288,
    'C' -> .0271,
    'M' -> .0261,
    'F' -> .0230,
    'Y' -> .0211,
    'W' -> .0209,
    'G' -> .0203,
    'P' -> .0182,
    'B' -> .0149,
    'V' -> .0111,
    'K' -> .0069,
    'X' -> .0017,
    'Q' -> .0011,
    'J' -> .0010,
    'Z' -> .0007
  )
  // http://www.cryptograms.org/letter-frequencies.php#Bigrams
  val bigramFrequencies = Map(
    "TH" -> .03882543,
    "HE" -> .03681391,
    "IN" -> .02283899,
    "ER" -> .02178042,
    "AN" -> .02140460,
    "RE" -> .01749394,
    "ND" -> .01571977,
    "ON" -> .01418244,
    "EN" -> .01383239,
    "AT" -> .01335523,
    "OU" -> .01285484,
    "ED" -> .01275779,
    "HA" -> .01274742,
    "TO" -> .01169655,
    "OR" -> .01151094,
    "IT" -> .01134891,
    "IS" -> .01109877,
    "HI" -> .01092302,
    "ES" -> .01092301,
    "NG" -> .01053385
  )
  // http://www.cryptograms.org/letter-frequencies.php#Trigrams
  val trigramFrequencies = Map(
    "THE" ->  .03508232,
    "AND" ->  .01593878,
    "ING" ->  .01147042,
    "HER" ->  .00822444,
    "HAT" ->  .00650715,
    "HIS" ->  .00596748,
    "THA" ->  .00593593,
    "ERE" ->  .00560594,
    "FOR" ->  .00555372,
    "ENT" ->  .00530771,
    "ION" ->  .00506454,
    "TER" ->  .00461099,
    "WAS" ->  .00460487,
    "YOU" ->  .00437213,
    "ITH" ->  .00431250,
    "VER" ->  .00430732,
    "ALL" ->  .00422758,
    "WIT" ->  .00397290,
    "THI" ->  .00394796,
    "TIO" ->  .00378058
  )
} with Language

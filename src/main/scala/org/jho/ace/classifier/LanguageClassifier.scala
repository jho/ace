/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.classifier

import org.jho.ace.util.Configuration
import org.jho.ace.util.LogHelper

trait LanguageClassifier extends Configuration with LogHelper {
  def classify(text:String):Boolean

  def score(text:String):Float

  def load:Boolean

  def train:Unit
}

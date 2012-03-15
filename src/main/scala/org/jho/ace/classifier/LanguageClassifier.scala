/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.classifier

trait LanguageClassifier {
    def predict(text:String):Boolean

    def score(text:String):Float

    def load:Boolean

    def train:Unit
}

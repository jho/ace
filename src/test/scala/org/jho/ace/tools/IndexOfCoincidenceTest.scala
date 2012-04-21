/*
 * Copyright 2012 Photobucket 
 */

package org.jho.ace.tools

import org.jho.ace.util.Configureable
import org.jho.ace.util._

import org.junit._
import Assert._

class IndexOfCoincidenceTest extends Configureable {

    @Test
    def example = {
        100.times {
            println(indexOfCoincidence(language.sample(100)))
        }
    }

}

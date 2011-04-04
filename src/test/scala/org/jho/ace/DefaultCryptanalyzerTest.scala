/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

import org.jho.ace.util.Configuration

class DefaultCryptanalyzerTest extends Configuration {

  @Test
  def decrypt = {
    var ca = new DefaultCryptanalyzer("QPWKALVRXCQZIKGRBPFAEOMFLJMSDZVDHXCXJYEBIMTRQWNMEAIZRVKCVKVLXNEICFZPZCZZHKMLVZVZIZRRQWDKECHOSNYXXLSPMYKVQXJTDCIOMEEXDQVSRXLRLKZHOV")
    var result = ca.decrypt
    println(result)
  }

}

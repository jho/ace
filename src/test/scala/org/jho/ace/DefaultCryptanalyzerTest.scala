/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace

import org.junit._
import Assert._

class DefaultCryptanalyzerTest {

  @Test
  def decrypt = {
    var ca = new DefaultCryptanalyzer("QPWKALVRXCQZIKGRBPFAEOMFLJMSDZVDHXCXJYEBIMTRQWNMEAIZRVKCVKVLXNEICFZPZCZZHKMLVZVZIZRRQWDKECHOSNYXXLSPMYKVQXJTDCIOMEEXDQVSRXLRLKZHOV")
    var result = ca.decrypt
    println(result)
  }

}

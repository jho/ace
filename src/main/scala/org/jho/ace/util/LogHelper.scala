/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.util

import org.apache.log4j.Logger

trait LogHelper {
  lazy val logger = Logger.getLogger(this.getClass)
}

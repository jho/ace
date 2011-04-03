/*
 * File: Dictionary.java
 *
 * Author: Joshua Hollander (jhollander@localmatters.com)
 * Created: Apr 2, 2011 9:31:42 PM
 *
 * Copyright 2010 Local Matters, Inc.
 *
 * Last checkin:
 *  $Author:$
 *  $Revision:$
 *  $Date:$
 */
package org.jho.ace.util

object Dictionary {
    lazy val words:Set[String] = scala.io.Source.fromInputStream(getClass.getResourceAsStream("/words")).getLines.toSet
}

/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.ui

import org.bowlerframework.controller._
import org.bowlerframework.view._
import org.bowlerframework._

class AceController extends Controller with Renderable {
  get("/")({ (request, response) =>
      render
    })

  post("/")({ (request, response) =>
      render
    })
}

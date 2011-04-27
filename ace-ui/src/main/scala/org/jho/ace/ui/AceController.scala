/*
 * Copyright 2011 Joshua Hollander.
 */
package org.jho.ace.ui

import org.jho.ace._
import org.jho.ace.ciphers._
import org.jho.ace.util._

import org.bowlerframework.controller._
import org.bowlerframework.view._
import org.bowlerframework._

class AceController extends Controller with Renderable with LogHelper {
  get("/")({ (request, response) =>
      render
    })

  get("/encrypt")({ (request, response) =>
      var plainText = request.getStringParameter("plainText")
      var keyword = request.getStringParameter("keyword")
      var cipher = new Vigenere
      render(cipher.encrypt(keyword, plainText))
    })


  get("/decrypt")({ (request, response) =>
      logger.info("decypt called...")
      var cipherText = request.getStringParameter("cipherText")
      var algorithm = "astar"
      if (request.getParameterNames.exists(_.equals("algorithm"))) {
        algorithm = request.getStringParameter("algorithm")
      }
      var ca = (algorithm.toLowerCase match {
        case "sa" => new SACryptanalyzer
        case _ => new AStarCryptanalyzer
      })
      logger.info("using " + ca.getClass.getName)
      var cipher = new Vigenere
      var result = ca.decrypt(cipherText, cipher)
      logger.info("decryption complete!")
      render(result)
    })
}

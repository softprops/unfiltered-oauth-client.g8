package com.example

import unfiltered.request._
import unfiltered.response._

import dispatch._
import dispatch.oauth._
import dispatch.oauth.OAuth._

import org.clapper.avsl.Logger

class App(consumer: Consumer) extends Templates with unfiltered.filter.Plan {
  import QParams._
  val log = Logger(classOf[App])
  val srvc = :/("localhost", 8080) / "oauth"
  val tmap = scala.collection.mutable.Map.empty[String, ClientToken]
  
  def intent = {
    
    case GET(Path("/", r)) => tokenList(tmap.values)
    
    // kickoff for oauth dance party
    case GET(Path("/connect", _)) =>
      val token = Http(srvc.POST / "request_token" <@ (consumer, "http://localhost:8081/authorized") as_token)
      log.info("fetched token unauthorized request token %s" format token.value)
      tmap += (token.value -> RequestToken(token.value, token.secret))
      Redirect("http://localhost:8080/oauth/authorize?oauth_token=%s" format(token.value))
      
    // post user authorization callback uri
    case GET(Path("/authorized", Params(params, r))) =>
      val expected = for {
        verifier <- lookup("oauth_verifier") is required("verifier is required") is nonempty("token can not be blank")
        token <- lookup("oauth_token") is required("token is required") is nonempty("token can not be blank")
      } yield {
        log.info("recieved authorization for token %s from verifier %s" format(token.get, verifier.get))
        val access_token = Http(srvc.POST / "access_token" <@ (consumer, tmap(token.get).asDispatchToken, verifier.get) as_token)
        log.info("fetched access token %s" format access_token.value)
        tmap -= token.get
        tmap += (access_token.value -> AccessToken(access_token.value, access_token.secret, verifier.get))
        Redirect("/")
      }
      
      expected(params) orFail { fails =>
        BadRequest ~> ResponseString(fails.map { _.error } mkString(". "))
      }
  }
}
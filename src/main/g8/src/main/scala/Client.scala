package com.example

import unfiltered.request._
import unfiltered.response._

import org.clapper.avsl.Logger
import dispatch._
import dispatch.oauth._
import dispatch.oauth.OAuth._

/** oauth client */
object Client {
  val log = Logger(Server.getClass)
  val port = 8081
  def resources = new java.net.URL(getClass.getResource("/web/robots.txt"), ".")
  
  trait Templates {
    def page(body: scala.xml.NodeSeq) = Html(
      <html>
        <head>
          <title>oauth client</title>
          <link href="/css/app.css" type="text/css" rel="stylesheet"/>
        </head>
        <body>
          <div id="container">
            <h1>OAuth Client</h1>
            {body}
          </div>
        </body>
      </html>
    )
  }
  
  class App(consumer: Consumer) extends Templates with unfiltered.filter.Plan {
    import QParams._
    val log = Logger(classOf[App])
    val srvc = :/("localhost", 8080) / "oauth"
    val tmap = scala.collection.mutable.Map.empty[String, Token]
    
    def intent = {
      
      // index
      case GET(Path("/", _)) =>
        page(<a href={"http://localhost:%s/go" format Client.port} >start dance</a>)
      
      // kickoff for oauth dance party
      case GET(Path("/go", _)) =>
        val token = Http(srvc.POST / "request_token" <@ (consumer, "http://localhost:8081/authorized") as_token)
        log.info("fetched token unauthorized request token %s" format token)
        tmap += (token.value -> token)
        Redirect("http://localhost:8080/oauth/authorize?oauth_token=%s" format(token.value))
        
      // post user authorization callback uri
      case GET(Path("/authorized", Params(params, r))) =>
        val expected = for {
          verifier <- lookup("oauth_verifier") is required("verifier is required") is nonempty("token can not be blank")
          token <- lookup("oauth_token") is required("token is required") is nonempty("token can not be blank")
        } yield {
          log.info("recieved authorization for token %s from verifier %s" format(token.get, verifier.get))
          val access_token = Http(srvc.POST / "access_token" <@ (consumer, tmap(token.get), verifier.get) as_token)
          log.info("fetched access token %s" format access_token)
          page(<h2>{"got access token token %s. mwhahaha" format access_token.value}</h2>)
        }
        
        expected(params) orFail { fails =>
          BadRequest ~> ResponseString("opps")
        }
    }
  }
  
  def main(args: Array[String]) {
    log.info("starting unfiltered oauth consumer at localhost on port %s" format port)
    unfiltered.jetty.Http(port)
      .resources(Client.resources)
      .filter(new App(Consumer("key", "secret"))).run
  }
}

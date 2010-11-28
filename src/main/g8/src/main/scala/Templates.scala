package com.example

import unfiltered.response.Html
import dispatch.oauth.Token

trait Templates {
  
  def page(body: scala.xml.NodeSeq) = Html(
    <html>
      <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <title>oauth client</title>
        <link href="/css/app.css" type="text/css" rel="stylesheet"/>
      </head>
      <body>
        <div id="container">
          <h1><a href="/">oauth client</a></h1>
          {body}
        </div>
      </body>
    </html>
  )
  
  def index = page(<a href={"http://localhost:%s/" format Client.port} >connect with provider</a>)
  
  def tokenList(toks: Iterable[ClientToken]) = page(
    <div>
      <a href={"http://localhost:%s/connect" format Client.port} >connect with provider</a>
      <p>tokens</p>
      <ul>{ toks.map { t => t match {
        case RequestToken(value,_) => <li><strong>{ value }</strong> (request)</li> 
        case AccessToken(value,_,_) => <li><strong>{ value }</strong> (access)</li> 
        } } }
      { if(toks.isEmpty) <li>None</li> }
      </ul>
    </div>
  )
}
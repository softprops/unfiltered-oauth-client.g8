package com.example

trait ClientToken {
  val value: String
  val secret: String
  def asDispatchToken = dispatch.oauth.Token(value, secret)
}
case class RequestToken(value: String, secret: String) extends ClientToken
case class AccessToken(value: String, secret: String, verifier: String) extends ClientToken

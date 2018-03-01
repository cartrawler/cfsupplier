package com.cabforce.service


import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContextExecutor


object HttpMicroservice extends App with Service {
  override implicit val system : ActorSystem = ActorSystem()
  override implicit val executor : ExecutionContextExecutor = system.dispatcher
  override implicit val materializer : Materializer = ActorMaterializer()

  override val config = ConfigFactory.load()
  override val logger = Logging(system, getClass)

  Http().bindAndHandle(routes, config.getString("http.interface"), config.getInt("http.port"))
}
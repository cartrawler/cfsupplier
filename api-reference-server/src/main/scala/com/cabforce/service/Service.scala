package com.cabforce.service

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.stream.Materializer
import com.cabforce.core._
import com.typesafe.config.Config

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

object SearchRoute extends Protocols {

  implicit val system : ActorSystem = ActorSystem()
  implicit val executionContext: ExecutionContext = system.dispatcher

  private val flow = new ReferenceApiSearchFlow()

  implicit def searchExceptionHandle : ExceptionHandler = ExceptionHandler {
    case all : Exception => extractUri { uri =>
     complete(DefaultResponse(Failed, Option(s"Search ${uri.toString} error : ${all.getMessage}"), Option.empty))
    }
  }

  val route: Config => Route = (config: Config) => pathPrefix("pricing") {
   handleExceptions(searchExceptionHandle) {
     path("search") {
       (post & entity(as[SearchRequest])) { searchRequest =>
         complete {
           flow.searchQ(config, searchRequest)
         }
       }
     }
   }
 }
}

object BookingRoute extends Protocols {

  private val flow = new ReferenceApiStateChangeFlow()

  val route: Config => Route = (config: Config) => pathPrefix("booking") {
    path("create") {
      (post & entity(as[Create])) { create =>
        complete {
          flow.createQ(create)
        }
      }
    } ~
    path("cancel") {
      (post & entity(as[Booking])) { booking =>
        complete {
          flow.cancelQ(booking)
        }
      }
    } ~
    path("status") {
      (post & entity(as[Booking])) { booking =>
        complete {
          flow.statusQ(booking)
        }
      }
    }
  }
}


trait Service extends Protocols {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer

  def config: Config
  val logger: LoggingAdapter

  val routes: Route = {
    logRequestResult("cfapiserver") {
      pathPrefix("v1") { SearchRoute.route(config) }  ~ pathPrefix("v1") { BookingRoute.route(config) } ~ pathPrefix("") { complete { DefaultResponse(Failed, Option(s"No such route"),  Option.empty) } }
    }
  }
}


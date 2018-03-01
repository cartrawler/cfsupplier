package com.cabforce.service

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

import akka.actor.ActorSystem
import akka.event.Logging
import com.cabforce.cache.SearchObjectCache
import com.cabforce.core._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class ReferenceApiStateChangeFlow() {

  implicit val system : ActorSystem = ActorSystem()
  implicit val executionContext: ExecutionContext = system.dispatcher
  private val logger = Logging(system, getClass)

  private val timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmZ")

  def createQ(create: Create): Future[Option[BookingCreateResponse]] = {
    Try(SearchObjectCache.scache(create.bookingRefId)) match {
      case Success(v) => Future(Option(BookingCreateResponse(Ok, create.bookingRefId, OffsetDateTime.now.plusDays(14).format(timeFormat), Option.empty, Option.empty)))
      case Failure(v) => Try(SearchObjectCache.scache(create.supplierPricingRefId.getOrElse(""), create.bookingRefId, BookingStatusEntry(create, Open)))  match {
        case Success(a) => Future(Option(BookingCreateResponse(Ok, create.bookingRefId, OffsetDateTime.now.plusDays(14).format(timeFormat), Option.empty, Option.empty)))
        case Failure(a) => Future(Option(BookingCreateResponse(Failed, "", "", Option(s"Booking ${create.bookingRefId} create failed - ${a.getMessage}"), Option.empty)))
      }
    }
  }

  def cancelQ(booking: Booking) : Future[Option[BookingCancelResponse]] = {
    Try {
      SearchObjectCache.supdate(booking.bookingRefId, SearchObjectCache.scache(booking.bookingRefId).copy(status = Cancelled))
      logger.debug(s"Booking CANCEL call done : ${booking.bookingRefId}")
    } match {
      case Success(v) => Future(Option(BookingCancelResponse(Ok, Option.empty, Option.empty)))
      case Failure(v) => Future(Option(BookingCancelResponse(Failed,  Option(s"Booking ${booking.bookingRefId} cancel failed - ${v.getMessage}"), Option.empty)))
    }
  }

  def statusQ(booking: Booking) : Future[Option[BookingStatusResponse]] = {
    Try {
      val current = SearchObjectCache.scache(booking.bookingRefId)
      logger.debug(s"Booking STATUS call done : ${booking.bookingRefId} - ${current.status}")
      current.status match {
        case Open => {
          booking.bookingRefId match {
            case "deadbeef" => SearchObjectCache.supdate(booking.bookingRefId, current.copy(status = Rejected))
            case _ => SearchObjectCache.supdate(booking.bookingRefId, current.copy(status = Ok))
          }
          BookingStatusResponse(Ok, current.status, Option.empty, Option.empty, Option.empty)
        }
        case Ok =>
          SearchObjectCache.supdate(booking.bookingRefId, current.copy(status = Finished))
          BookingStatusResponse(Ok, current.status, Option.empty, Option.empty, Option(DriveDetails("Mike the Pedal", "654321654")))
        case Rejected => BookingStatusResponse(Ok, current.status, Option.empty, Option.empty, Option.empty)
        case Cancelled => BookingStatusResponse(Ok, current.status, Option.empty, Option.empty, Option.empty)
        case Finished => BookingStatusResponse(Ok, current.status, Option.empty, Option.empty, Option.empty)
        case other => BookingStatusResponse(Ok, Failed, Option(s"Booking state ${other.state} error"), Option.empty, Option.empty)
      }
    } match {
      case Success(v) => Future(Option(v))
      case Failure(v) => Future(Option(BookingStatusResponse(Failed, Failed, Option(s"Booking ${booking.bookingRefId} error - ${v.getMessage}"), Option.empty, Option.empty)))
    }
  }
}

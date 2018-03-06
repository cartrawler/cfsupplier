package com.cabforce.service

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

import akka.actor.ActorSystem
import akka.event.Logging
import com.cabforce.cache.SearchObjectCache
import com.cabforce.core._
import com.typesafe.config.Config

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Random, Success, Try}

class ReferenceApiSearchFlow() {

  implicit val system : ActorSystem = ActorSystem()
  implicit val executionContext: ExecutionContext = system.dispatcher
  private val logger = Logging(system, getClass)

  private val timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmZ")
  private val rnd = new Random()

  private val rndRange: Int => Int = (range: Int) => rnd.nextInt(range)
  private val rndBaseRange: Int => Int => Int = (base: Int) => (range: Int) => base + rnd.nextInt(range)

  private val productGenerator : SearchRequest => Product = (searchRequest: SearchRequest) => {

    val km = rndBaseRange(20)(60)
    val meetAndGreet = rndBaseRange(10)(20)
    val key = Seq.fill(12)(rndRange(10)).mkString("")
    SearchObjectCache.pcache(key,
      com.cabforce.core.Product(key,
        ProductType(DriveTypeNames.names(rndRange(DriveTypeNames.names.size)),
        DriveCategoryTypeNames.names(rndRange(DriveCategoryTypeNames.names.size)),
        rndRange(4),
        rndRange(5),
        Option(Seq(ProductProperty(TaxiSign, Option(1), Option.empty),
          ProductProperty(Magazines, Option(1), Option.empty),
          ProductProperty(WaterBottles, Option(1), Option.empty))), "Tesla Model X"), Price(km, meetAndGreet, km + meetAndGreet, Seq("Terms of service defined")),
      Option(searchRequest.ageGroups.getOrElse(Seq(AgeGroup(0, 100, 100)))),
      Option.empty,
      Pickup("meet&greet", "TMX Cafe Helsinki", 30, Option.empty),
      "good", 30, 60,
      Option.empty,
      Option(Seq(Cancellation("free", 120, 100))),
      Option.empty,
      OffsetDateTime.now.plusDays(14).format(timeFormat)))
  }

  def searchQ(config: Config, searchRequest : SearchRequest) : Future[Option[SearchResult]] = {
    Try {
      logger.debug(s"Offer SEARCH call done : $searchRequest")
      val offerSeq = Seq.fill(rndBaseRange(1)(5))(productGenerator(searchRequest))
      logger.debug(s"Offer result set done : $offerSeq")
      if (config.getLong("request.search.sleep") != 0L) Thread.sleep(config.getLong("request.search.sleep") + rnd.nextInt(config.getInt("request.search.variance")))
      offerSeq
    } match {
      case Success(v) => Future(Option(SearchResult(Ok, Option(v))))
      case Failure(v) => throw new Exception(s"${v.getLocalizedMessage}")
    }
  }
}

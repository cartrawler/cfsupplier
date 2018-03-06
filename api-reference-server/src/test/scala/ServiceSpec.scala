import java.time.format.DateTimeFormatter
import java.time.OffsetDateTime

import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.cabforce.core._
import com.cabforce.service.Service
import com.typesafe.config.Config
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable.ArrayBuffer

class ServiceSpec extends FlatSpec with Matchers with ScalatestRouteTest with Service {

  override def testConfigSource = "akka.loglevel = INFO"
  override def config : Config = testConfig
  override val logger : LoggingAdapter = Logging.getLogger(system, this)

  val timeFormat : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmZ")
  val addresses = ArrayBuffer(Address(1, "Helsinki vantaa", Location(60.31804959999999,24.96536479999), AirportCategory), Address(2, "Kaserngatan 40, 00130, Helsingfors, Finland", Location(60.16572189999999,24.9476088999), AddressCategory))
  val ageGroup = ArrayBuffer(AgeGroup(0, 18, 0), AgeGroup(18, 100, 2))
  val pickupTime : String = OffsetDateTime.now.plusDays(14).format(timeFormat)

  it should "respond with offer in request" in {

    Post(s"/v1/pricing/search", SearchRequest(pickupTime, addresses, Option(ageGroup))) ~> routes ~> check {
      val str = responseAs[SearchResult].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[SearchResult].status shouldBe Ok
    }
  }

  it should "confirm offer in request" in {

    val result = Post(s"/v1/pricing/search", SearchRequest(pickupTime, addresses, Option(ageGroup))) ~> routes ~> check {
      val str = responseAs[SearchResult].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[SearchResult].status shouldBe Ok
      responseAs[SearchResult]
    }

    val formatter = java.text.NumberFormat.getCurrencyInstance
    val myId = "123456789"

    Post(s"/v1/booking/create", Create(myId,
      "123",
      Option(result.result.get(0).supplierPricingRefId),
      pickupTime,
      addresses,
      Option.empty,
      Option.empty,
      Option("pickupDetails"),
      Option(CreateProperty(Option("leg1"), Option("refe1"))),
      Seq(Passenger("Timmy Test", Option(""), "32343433")),
      Option("Passenger provided extra notes"),
      DriveProduct(result.result.get(0).product.`type`,
        result.result.get(0).product.category,
        result.result.get(0).product.bags,
        result.result.get(0).product.pax,
        Option.empty,
        Option.empty),
      DrivePrice(Option(formatter.format(result.result.get(0).price.supplier)),
        Option(formatter.format(result.result.get(0).price.service)), formatter.format(result.result.get(0).price.total),
        Option("EUR"), Option.empty),
      Option.empty
    )) ~> routes ~> check {
      val str = responseAs[BookingCreateResponse].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[BookingCreateResponse].status shouldBe Ok
    }
  }

  it should "confirm offer in request and check status" in {

    val result = Post(s"/v1/pricing/search", SearchRequest(pickupTime, addresses, Option(ageGroup))) ~> routes ~> check {
      val str = responseAs[SearchResult].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[SearchResult].status shouldBe Ok
      responseAs[SearchResult]
    }

    val formatter = java.text.NumberFormat.getCurrencyInstance
    val myId = "987654321"

    Post(s"/v1/booking/create", Create(myId,
      "321",
      Option(result.result.get(0).supplierPricingRefId),
      pickupTime,
      addresses,
      Option.empty,
      Option.empty,
      Option("pickupDetails"),
      Option(CreateProperty(Option("leg1"), Option("refe1"))),
      Seq(Passenger("Timmy Test", Option(""), "32343433")),
      Option("Passenger provided extra notes"),
      DriveProduct(result.result.get(0).product.`type`,
        result.result.get(0).product.category,
        result.result.get(0).product.bags,
        result.result.get(0).product.pax,
        Option.empty,
        Option.empty),
      DrivePrice(Option(formatter.format(result.result.get(0).price.supplier)),
        Option(formatter.format(result.result.get(0).price.service)), formatter.format(result.result.get(0).price.total),
        Option("EUR"), Option.empty),
      Option.empty
    )) ~> routes ~> check {
      val str = responseAs[BookingCreateResponse].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[BookingCreateResponse].status shouldBe Ok

    }

    Post(s"/v1/booking/status", Booking(myId)) ~> routes ~> check {
      val str = responseAs[BookingStatusResponse].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[BookingStatusResponse].state shouldBe Open
    }

    Post(s"/v1/booking/status", Booking(myId)) ~> routes ~> check {
      val str = responseAs[BookingStatusResponse].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[BookingStatusResponse].state shouldBe Ok
      responseAs[BookingStatusResponse].driverDetails.get.name shouldBe "Mike the Pedal"
      responseAs[BookingStatusResponse].driverDetails.get.phone shouldBe "654321654"
    }

    Post(s"/v1/booking/status", Booking(myId)) ~> routes ~> check {
      val str = responseAs[BookingStatusResponse].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[BookingStatusResponse].state shouldBe Finished
    }
  }

  it should "confirm offer in request and cancel after open" in {

    val result = Post(s"/v1/pricing/search", SearchRequest(pickupTime, addresses, Option(ageGroup))) ~> routes ~> check {
      val str = responseAs[SearchResult].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[SearchResult].status shouldBe Ok
      responseAs[SearchResult]
    }

    val formatter = java.text.NumberFormat.getCurrencyInstance
    val myId = "987654321ABC"

    Post(s"/v1/booking/create", Create(myId,
      "321",
      Option(result.result.get(0).supplierPricingRefId),
      pickupTime,
      addresses,
      Option.empty,
      Option.empty,
      Option("pickupDetails"),
      Option(CreateProperty(Option("leg1"), Option("refe1"))),
      Seq(Passenger("Timmy Test", Option(""), "32343433")),
      Option("Passenger provided extra notes"),
      DriveProduct(result.result.get(0).product.`type`,
        result.result.get(0).product.category,
        result.result.get(0).product.bags,
        result.result.get(0).product.pax,
        Option.empty,
        Option.empty),
      DrivePrice(Option(formatter.format(result.result.get(0).price.supplier)),
        Option(formatter.format(result.result.get(0).price.service)), formatter.format(result.result.get(0).price.total),
        Option("EUR"), Option.empty),
      Option.empty
    )) ~> routes ~> check {
      val str = responseAs[BookingCreateResponse].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[BookingCreateResponse].status shouldBe Ok

    }

    Post(s"/v1/booking/status", Booking(myId)) ~> routes ~> check {
      val str = responseAs[BookingStatusResponse].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[BookingStatusResponse].state shouldBe Open
    }

    Post(s"/v1/booking/cancel", Booking(myId)) ~> routes ~> check {
      val str = responseAs[BookingCancelResponse].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[BookingCancelResponse].status shouldBe Ok
    }

    Post(s"/v1/booking/status", Booking(myId)) ~> routes ~> check {
      val str = responseAs[BookingStatusResponse].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[BookingStatusResponse].state shouldBe Cancelled

    }
  }

  it should "confirm offer in request and reject after ok" in {

    val result = Post(s"/v1/pricing/search", SearchRequest(pickupTime, addresses, Option(ageGroup))) ~> routes ~> check {
      val str = responseAs[SearchResult].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[SearchResult].status shouldBe Ok
      responseAs[SearchResult]
    }

    val formatter = java.text.NumberFormat.getCurrencyInstance
    val myId = "deadbeef"

    Post(s"/v1/booking/create", Create(myId,
      "321",
      Option(result.result.get(0).supplierPricingRefId),
      pickupTime,
      addresses,
      Option.empty,
      Option.empty,
      Option("pickupDetails"),
      Option(CreateProperty(Option("leg1"), Option("refe1"))),
      Seq(Passenger("Timmy Test", Option(""), "32343433")),
      Option("Passenger provided extra notes"),
      DriveProduct(result.result.get(0).product.`type`,
        result.result.get(0).product.category,
        result.result.get(0).product.bags,
        result.result.get(0).product.pax,
        Option.empty,
        Option.empty),
      DrivePrice(Option(formatter.format(result.result.get(0).price.supplier)),
        Option(formatter.format(result.result.get(0).price.service)), formatter.format(result.result.get(0).price.total),
        Option("EUR"), Option.empty),
      Option.empty
    )) ~> routes ~> check {
      val str = responseAs[BookingCreateResponse].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[BookingCreateResponse].status shouldBe Ok

    }

    Post(s"/v1/booking/status", Booking(myId)) ~> routes ~> check {
      val str = responseAs[BookingStatusResponse].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[BookingStatusResponse].state shouldBe Open
    }

    Post(s"/v1/booking/status", Booking(myId)) ~> routes ~> check {
      val str = responseAs[BookingStatusResponse].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[BookingStatusResponse].state shouldBe Rejected
    }
  }

  it should "confirm offer twice and check status" in {

    val result = Post(s"/v1/pricing/search", SearchRequest(pickupTime, addresses, Option(ageGroup))) ~> routes ~> check {
      val str = responseAs[SearchResult].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[SearchResult].status shouldBe Ok
      responseAs[SearchResult]
    }

    val formatter = java.text.NumberFormat.getCurrencyInstance
    val myId = "987654321000"

    Post(s"/v1/booking/create", Create(myId,
      "321",
      Option(result.result.get(0).supplierPricingRefId),
      pickupTime,
      addresses,
      Option.empty,
      Option.empty,
      Option("pickupDetails"),
      Option(CreateProperty(Option("leg1"), Option("refe1"))),
      Seq(Passenger("Timmy Test", Option(""), "32343433")),
      Option("Passenger provided extra notes"),
      DriveProduct(result.result.get(0).product.`type`,
        result.result.get(0).product.category,
        result.result.get(0).product.bags,
        result.result.get(0).product.pax,
        Option.empty,
        Option.empty),
      DrivePrice(Option(formatter.format(result.result.get(0).price.supplier)),
        Option(formatter.format(result.result.get(0).price.service)), formatter.format(result.result.get(0).price.total),
        Option("EUR"), Option.empty),
      Option.empty
    )) ~> routes ~> check {
      val str = responseAs[BookingCreateResponse].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[BookingCreateResponse].status shouldBe Ok

    }

    Post(s"/v1/booking/create", Create(myId,
      "321",
      Option(result.result.get(0).supplierPricingRefId),
      pickupTime,
      addresses,
      Option.empty,
      Option.empty,
      Option("pickupDetails"),
      Option(CreateProperty(Option("leg1"), Option("refe1"))),
      Seq(Passenger("Timmy Test", Option(""), "32343433")),
      Option("Passenger provided extra notes"),
      DriveProduct(result.result.get(0).product.`type`,
        result.result.get(0).product.category,
        result.result.get(0).product.bags,
        result.result.get(0).product.pax,
        Option.empty,
        Option.empty),
      DrivePrice(Option(formatter.format(result.result.get(0).price.supplier)),
        Option(formatter.format(result.result.get(0).price.service)), formatter.format(result.result.get(0).price.total),
        Option("EUR"), Option.empty),
      Option.empty
    )) ~> routes ~> check {
      val str = responseAs[BookingCreateResponse].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[BookingCreateResponse].status shouldBe Ok

    }


    Post(s"/v1/booking/status", Booking(myId)) ~> routes ~> check {
      val str = responseAs[BookingStatusResponse].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[BookingStatusResponse].state shouldBe Open
    }

    Post(s"/v1/booking/status", Booking(myId)) ~> routes ~> check {
      val str = responseAs[BookingStatusResponse].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[BookingStatusResponse].state shouldBe Ok
      responseAs[BookingStatusResponse].driverDetails.get.name shouldBe "Mike the Pedal"
      responseAs[BookingStatusResponse].driverDetails.get.phone shouldBe "654321654"
    }

    Post(s"/v1/booking/status", Booking(myId)) ~> routes ~> check {
      val str = responseAs[BookingStatusResponse].toString
      logger.info(str)

      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[BookingStatusResponse].state shouldBe Finished
    }
  }

}

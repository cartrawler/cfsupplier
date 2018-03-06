package com.cabforce.core

import akka.actor.ActorSystem
import akka.event.Logging
import spray.json.{DefaultJsonProtocol, JsNumber, JsString, JsValue, RootJsonFormat}


object DrivePropertyTypeNames {
  val names : List[DrivePropertyType] = List(TaxiLicense, PrivateHireLicense,
    SharedCar, PrivateCar, ElectricCar, HybridCar, Wifi,
    WaterBottles, Newspapers, MaxPax, MaxLug, CoEmissions,
    ArrivalNotifications, ChildSeatType, AdditionalStop, PickupWaitTime,
    AdditionalWaitTime, CarMakeAndModel, DriverLanguages, TaxiSign,
    Magazines, DomesticWaitTime, InternationalWaitTime, ProductDescription,
    AutoAcceptAllowed, MobilePhoneCharger, SpecialDriverLanguage, TransferType,
    EngineType, ArmoredCar, WheelChair, Campaign, Tablet, SelfDrivenCar,
    FourWheelDrive, MaxLugPerPax, PickupTimeEstimated, MaxCabinLuggage, MaxSuitcaseLuggage,
    PickupLandmark, DropoffLandmark)
}

object DriveTypeNames {
  val names : List[DriveType] = List(Compact, Sedan, PeopleCarrier, SUV, VanOrMinibus,
    Coach, StretchLimo, StationWagon, Convertible, SportsCar, Offroad,
    PickupTruck, Motorcycle, Rickshaw, WaterTaxi, Shuttle)
}

object DriveCategoryTypeNames {
  val names : List[DriveCategoryType] =  List(Economy, Standard, Business,
    Premium, Luxury, SpeediShuttle)
}

object AddressCategoryNames {
  val names : List[AddressCategoryEnum] = List(AddressCategory, AirportCategory)
}

trait Protocols extends DefaultJsonProtocol {

  class BookingStatusJsonFormat extends RootJsonFormat[BookingStatusEnum] {

    implicit val system : ActorSystem = ActorSystem()
    val logger = Logging(system, getClass)

    def write(obj: BookingStatusEnum) : JsString = obj match {
      case t1: Ok.type => JsString(Ok.state)
      case t1: Open.type => JsString(Open.state)
      case t1: Cancelled.type => JsString(Cancelled.state)
      case t1: Rejected.type => JsString(Rejected.state)
      case t1: Finished.type => JsString(Finished.state)
      case t1: Failed.type => JsString(Failed.state)
      case _ => throw new RuntimeException("Unsupported conversion")
    }

    def read(json: JsValue) : BookingStatusEnum = {
      val discrimator = List(Ok.state, Open.state, Cancelled.state, Rejected.state, Finished.state).map(d => json.convertTo[String].equalsIgnoreCase(d))
      discrimator.indexOf(true) match {
        case 0 => Ok
        case 1 => Open
        case 2 => Cancelled
        case 3 => Rejected
        case 4 => Finished
        case 5 => Failed
        case _ => throw new RuntimeException("Unsupported conversion")
      }
    }
  }

  implicit val bookingStatusJsonFormat : BookingStatusJsonFormat = new BookingStatusJsonFormat()

  class DrivePropertyTypeJsonFormat extends RootJsonFormat[DrivePropertyType] {

    implicit val system : ActorSystem = ActorSystem()
    val logger = Logging(system, getClass)

    val typeList : List[Int] = DrivePropertyTypeNames.names.map(a => a.`type`)

    def write(obj: DrivePropertyType) : JsNumber = obj match {
      case t1: TaxiLicense.type => JsNumber(t1.`type`)
      case t1: PrivateHireLicense.type => JsNumber(t1.`type`)
      case t1: SharedCar.type => JsNumber(t1.`type`)
      case t1: PrivateCar.type => JsNumber(t1.`type`)
      case t1: ElectricCar.type => JsNumber(t1.`type`)
      case t1: HybridCar.type => JsNumber(t1.`type`)
      case t1: Wifi.type => JsNumber(t1.`type`)
      case t1: WaterBottles.type => JsNumber(t1.`type`)
      case t1: Newspapers.type => JsNumber(t1.`type`)
      case t1: MaxPax.type => JsNumber(t1.`type`)
      case t1: MaxLug.type => JsNumber(t1.`type`)
      case t1: CoEmissions.type => JsNumber(t1.`type`)
      case t1: ArrivalNotifications.type => JsNumber(t1.`type`)
      case t1: ChildSeatType.type => JsNumber(t1.`type`)
      case t1: AdditionalStop.type => JsNumber(t1.`type`)
      case t1: PickupWaitTime.type => JsNumber(t1.`type`)
      case t1: AdditionalWaitTime.type => JsNumber(t1.`type`)
      case t1: CarMakeAndModel.type => JsNumber(t1.`type`)
      case t1: DriverLanguages.type => JsNumber(t1.`type`)
      case t1: TaxiSign.type => JsNumber(t1.`type`)
      case t1: Magazines.type => JsNumber(t1.`type`)
      case t1: DomesticWaitTime.type => JsNumber(t1.`type`)
      case t1: InternationalWaitTime.type => JsNumber(t1.`type`)
      case t1: ProductDescription.type => JsNumber(t1.`type`)
      case t1: AutoAcceptAllowed.type => JsNumber(t1.`type`)
      case t1: MobilePhoneCharger.type => JsNumber(t1.`type`)
      case t1: SpecialDriverLanguage.type => JsNumber(t1.`type`)
      case t1: TransferType.type => JsNumber(t1.`type`)
      case t1: EngineType.type => JsNumber(t1.`type`)
      case t1: ArmoredCar.type => JsNumber(t1.`type`)
      case t1: WheelChair.type => JsNumber(t1.`type`)
      case t1: Campaign.type => JsNumber(t1.`type`)
      case t1: Tablet.type => JsNumber(t1.`type`)
      case t1: SelfDrivenCar.type => JsNumber(t1.`type`)
      case t1: FourWheelDrive.type => JsNumber(t1.`type`)
      case t1: MaxLugPerPax.type => JsNumber(t1.`type`)
      case t1: PickupTimeEstimated.type => JsNumber(t1.`type`)
      case t1: MaxCabinLuggage.type => JsNumber(t1.`type`)
      case t1: MaxSuitcaseLuggage.type => JsNumber(t1.`type`)
      case t1: PickupLandmark.type => JsNumber(t1.`type`)
      case t1: DropoffLandmark.type => JsNumber(t1.`type`)
      case _ => throw new RuntimeException("Unsupported conversion")
    }

    def read(json: JsValue) : DrivePropertyType = {
      val discrimator = typeList.map(d => json.convertTo[Int].equals(d))
      discrimator.indexOf(true) match {
        case 0 => TaxiLicense
        case 1 => PrivateHireLicense
        case 2 => SharedCar
        case 3 => PrivateCar
        case 4 => ElectricCar
        case 5 => HybridCar
        case 6 => Wifi
        case 7 => WaterBottles
        case 8 => Newspapers
        case 9 => MaxPax
        case 10 => MaxLug
        case 11 => CoEmissions
        case 12 => ArrivalNotifications
        case 13 => ChildSeatType
        case 14 => AdditionalStop
        case 15 => PickupWaitTime
        case 16 => AdditionalWaitTime
        case 17 => CarMakeAndModel
        case 18 => DriverLanguages
        case 19 => TaxiSign
        case 20 => Magazines
        case 21 => DomesticWaitTime
        case 22 => InternationalWaitTime
        case 23 => ProductDescription
        case 24 => AutoAcceptAllowed
        case 25 => MobilePhoneCharger
        case 26 => SpecialDriverLanguage
        case 27 => TransferType
        case 28 => EngineType
        case 29 => ArmoredCar
        case 30 => WheelChair
        case 31 => Campaign
        case 32 => Tablet
        case 33 => SelfDrivenCar
        case 34 => FourWheelDrive
        case 35 => MaxLugPerPax
        case 36 => PickupTimeEstimated
        case 37 => MaxCabinLuggage
        case 38 => MaxSuitcaseLuggage
        case 39 => PickupLandmark
        case 40 => DropoffLandmark
        case _ => throw new RuntimeException("Unsupported conversion")
      }
    }
  }

  implicit val drivePropertyTypeJsonFormat : DrivePropertyTypeJsonFormat = new DrivePropertyTypeJsonFormat()

  class DriveTypeJsonFormat extends RootJsonFormat[DriveType] {

    implicit val system : ActorSystem = ActorSystem()
    val logger = Logging(system, getClass)

    val typeList : List[Int] = DriveTypeNames.names.map(a => a.`type`)

    def write(obj: DriveType) : JsNumber = obj match {
      case t1: Compact.type => JsNumber(t1.`type`)
      case t1: Sedan.type => JsNumber(t1.`type`)
      case t1: PeopleCarrier.type => JsNumber(t1.`type`)
      case t1: SUV.type => JsNumber(t1.`type`)
      case t1: VanOrMinibus.type => JsNumber(t1.`type`)
      case t1: Coach.type => JsNumber(t1.`type`)
      case t1: StretchLimo.type => JsNumber(t1.`type`)
      case t1: StationWagon.type => JsNumber(t1.`type`)
      case t1: Convertible.type => JsNumber(t1.`type`)
      case t1: SportsCar.type => JsNumber(t1.`type`)
      case t1: Offroad.type => JsNumber(t1.`type`)
      case t1: PickupTruck.type => JsNumber(t1.`type`)
      case t1: Motorcycle.type => JsNumber(t1.`type`)
      case t1: Rickshaw.type => JsNumber(t1.`type`)
      case t1: WaterTaxi.type => JsNumber(t1.`type`)
      case t1: Shuttle.type => JsNumber(t1.`type`)
      case _ => throw new RuntimeException("Unsupported conversion")
    }

    def read(json: JsValue) : DriveType = {
      val discrimator = typeList.map(d => json.convertTo[Int].equals(d))
      discrimator.indexOf(true) match {
        case 0 => Compact
        case 1 => Sedan
        case 2 => PeopleCarrier
        case 3 => SUV
        case 4 => VanOrMinibus
        case 5 => Coach
        case 6 => StretchLimo
        case 7 => StationWagon
        case 8 => Convertible
        case 9 => SportsCar
        case 10 => Offroad
        case 11 => PickupTruck
        case 12 => Motorcycle
        case 13 => Rickshaw
        case 14 => WaterTaxi
        case 15 => Shuttle
        case _ => throw new RuntimeException("Unsupported conversion")
      }
    }
  }

  implicit val driveTypeJsonFormat : DriveTypeJsonFormat = new DriveTypeJsonFormat()

  class DriveCategoryTypeJsonFormat extends RootJsonFormat[DriveCategoryType] {

    implicit val system : ActorSystem = ActorSystem()
    val logger = Logging(system, getClass)

    val typeList : List[Int] = DriveCategoryTypeNames.names.map(a => a.`type`)

    def write(obj: DriveCategoryType) : JsNumber = obj match {
      case t1: Economy.type => JsNumber(t1.`type`)
      case t1: Standard.type => JsNumber(t1.`type`)
      case t1: Business.type => JsNumber(t1.`type`)
      case t1: Premium.type => JsNumber(t1.`type`)
      case t1: Luxury.type => JsNumber(t1.`type`)
      case t1: SpeediShuttle.type => JsNumber(t1.`type`)
      case _ => throw new RuntimeException("Unsupported conversion")
    }

    def read(json: JsValue) : DriveCategoryType = {
      val discrimator = typeList.map(d => json.convertTo[Int].equals(d))
      discrimator.indexOf(true) match {
        case 0 => Economy
        case 1 => Standard
        case 2 => Business
        case 3 => Premium
        case 4 => Luxury
        case 5 => SpeediShuttle
        case _ => throw new RuntimeException("Unsupported conversion")
      }
    }
  }

  implicit val driveCategoryTypeJsonFormat : DriveCategoryTypeJsonFormat = new DriveCategoryTypeJsonFormat()

  class AddressCategoryEnumJsonFormat extends RootJsonFormat[AddressCategoryEnum] {

    implicit val system : ActorSystem = ActorSystem()
    val logger = Logging(system, getClass)

    val typeList : List[String] = AddressCategoryNames.names.map(a => a.`type`)

    def write(obj: AddressCategoryEnum) : JsString = obj match {
      case t1: AddressCategory.type => JsString(t1.`type`)
      case t1: AirportCategory.type => JsString(t1.`type`)
      case _ => throw new RuntimeException("Unsupported conversion")
    }

    def read(json: JsValue) : AddressCategoryEnum = {
      val discrimator = typeList.map(d => json.convertTo[String].equalsIgnoreCase(d))
      discrimator.indexOf(true) match {
        case 0 => AddressCategory
        case 1 => AirportCategory
        case _ => throw new RuntimeException("Unsupported conversion")
      }
    }
  }

  implicit val addressCategoryEnumJsonFormat : AddressCategoryEnumJsonFormat = new AddressCategoryEnumJsonFormat()

  implicit val locationFormat : RootJsonFormat[Location] = jsonFormat2(Location.apply)
  implicit val addressFormat : RootJsonFormat[Address] = jsonFormat4(Address.apply)
  implicit val ageGroupFormat : RootJsonFormat[AgeGroup] = jsonFormat3(AgeGroup.apply)
  implicit val searchRequestFormat : RootJsonFormat[SearchRequest] = jsonFormat3(SearchRequest.apply)


  implicit val meetupIntroductionFormat : RootJsonFormat[MeetupIntroduction] = jsonFormat2(MeetupIntroduction.apply)
  implicit val meetupIntroductionSetFormat : RootJsonFormat[MeetupIntroductionSet] = jsonFormat1(MeetupIntroductionSet.apply)

  implicit val productPropertyFormat : RootJsonFormat[ProductProperty] = jsonFormat3(ProductProperty.apply)
  implicit val productTypeFormat : RootJsonFormat[ProductType] = jsonFormat6(ProductType.apply)
  implicit val pickupFormat : RootJsonFormat[Pickup] = jsonFormat4(Pickup.apply)
  implicit val cancellationFormat : RootJsonFormat[Cancellation] = jsonFormat3(Cancellation.apply)
  implicit val changesFormat : RootJsonFormat[Changes] = jsonFormat3(Changes.apply)
  implicit val priceFormat : RootJsonFormat[Price] = jsonFormat4(Price.apply)
  implicit val productFormat : RootJsonFormat[Product] = jsonFormat13(Product.apply)
  implicit val searchResultFormat : RootJsonFormat[SearchResult] = jsonFormat2(SearchResult.apply)

  implicit val flightFormat : RootJsonFormat[Flight] = jsonFormat4(Flight.apply)
  implicit val extraWaitingTimeFormat : RootJsonFormat[ExtraWaitingTime] = jsonFormat2(ExtraWaitingTime.apply)
  implicit val pickupOptionFormat : RootJsonFormat[PickupOption] = jsonFormat4(PickupOption.apply)
  implicit val createPropertyFormat : RootJsonFormat[CreateProperty] = jsonFormat2(CreateProperty.apply)
  implicit val passengerFormat : RootJsonFormat[Passenger] = jsonFormat3(Passenger.apply)
  implicit val driveProductFormat : RootJsonFormat[DriveProduct] = jsonFormat6(DriveProduct.apply)
  implicit val drivePriceFormat : RootJsonFormat[DrivePrice] = jsonFormat5(DrivePrice.apply)
  implicit val createFormat : RootJsonFormat[Create] = jsonFormat14(Create.apply)

  implicit val bookingCreateResponseFormat : RootJsonFormat[BookingCreateResponse] = jsonFormat5(BookingCreateResponse.apply)
  implicit val bookingFormat : RootJsonFormat[Booking] = jsonFormat1(Booking.apply)

  implicit val driveDetailsFormat : RootJsonFormat[DriveDetails] = jsonFormat2(DriveDetails.apply)

  implicit val bookingStatusResponseFormat : RootJsonFormat[BookingStatusResponse] = jsonFormat5(BookingStatusResponse.apply)
  implicit val bookingCancelResponseFormat : RootJsonFormat[BookingCancelResponse] = jsonFormat3(BookingCancelResponse.apply)
  implicit val defaultResponse : RootJsonFormat[DefaultResponse] = jsonFormat3(DefaultResponse.apply)
}


// Offer search request
case class Location(lat: Double, lng: Double)
case class Address(`type`: Int, address: String, location: Location, category: AddressCategoryEnum)
case class AgeGroup(minAge: Int, maxAge: Int, count: Int)
case class SearchRequest(pickupDateTime: String, addresses: Seq[Address], ageGroups: Option[Seq[AgeGroup]])

// Offer response
case class ProductProperty(`type`: DrivePropertyType, numberValue : Option[Int], stringValue: Option[String])
case class ProductType(`type` : DriveType, category: DriveCategoryType, pax: Int, bags: Int, properties: Option[Seq[ProductProperty]], makeModel: String)
case class Price(supplier: Double, service: Double, total: Double, included : Seq[String])
case class MeetupIntroduction(lang: String, text: String)
case class MeetupIntroductionSet(introduction: MeetupIntroduction)
case class Pickup(`type`: String, meetingPoint : String, waitingTime: Int, meetupIntroduction : Option[MeetupIntroductionSet])
case class Cancellation(`type`: String, maxTime: Int, percentage: Double)
case class Changes(`type`: String, maxTime: Int, fixed: Double)
case class Product(supplierPricingRefId: String, product: ProductType, price: Price, ageGroups: Option[Seq[AgeGroup]], rateId: Option[String], pickup: Pickup, capacity: String, duration: Int, distance: Int, terms: Option[String], cancellation: Option[Seq[Cancellation]], changes: Option[Changes], validUntil: String)
case class SearchResult(status: BookingStatusEnum, result: Option[Seq[Product]])

// Booking creation request
case class Flight(flightNumber: String, trackingNumber: String, `type`: Int, dateTime: String)
case class ExtraWaitingTime(chargePerHour: String, minimumCharge: String)
case class PickupOption(`type`: String, meetingPoint: String, waitingTime: String, extraWaitingTime: Option[ExtraWaitingTime])
case class CreateProperty(leg: Option[String], reference: Option[String])
case class Passenger(name: String, email: Option[String], phone: String)
case class DriveProduct(`type`: DriveType, category: DriveCategoryType, pax: Int, bags: Int, properties: Option[Seq[ProductProperty]], makeModel: Option[String])
case class DrivePrice(supplier: Option[String], service: Option[String], total: String, currency: Option[String], included: Option[Seq[String]])
case class Create(bookingRefId: String, shortId: String, supplierPricingRefId: Option[String],
                  pickupDateTime: String, addresses: Seq[Address], flights: Option[Seq[Flight]],
                  pickupOption: Option[PickupOption], pickupDetails: Option[String], createProperty: Option[CreateProperty],
                  passengers : Seq[Passenger], notes: Option[String], product: DriveProduct,
                  price: DrivePrice, ageGroups: Option[Seq[AgeGroup]])

// Default response
case class BookingCreateResponse(status: BookingStatusEnum, supplierBookingRefId: String, latestConfirmationTime: String, reason: Option[String], reasonType: Option[Int])

// Booking status request
// Booking cancel request
case class Booking(bookingRefId: String)

// Booking status response
sealed trait BookingStatusEnum { def state : String }
case object Open extends BookingStatusEnum { val state = "open" }
case object Ok extends BookingStatusEnum { val state = "ok" }
case object Cancelled extends BookingStatusEnum { val state = "cancelled" }
case object Rejected extends BookingStatusEnum { val state = "rejected" }
case object Finished extends BookingStatusEnum { val state = "finished" }
case object Failed extends BookingStatusEnum { val state = "failed" }
case class DriveDetails(name: String, phone: String)
case class BookingStatusResponse(status: BookingStatusEnum, state: BookingStatusEnum, reason: Option[String], reasonType: Option[Int], driverDetails: Option[DriveDetails])

// Booking cancel response
case class BookingCancelResponse(status: BookingStatusEnum, reason: Option[String], reasonType: Option[Int])

// Default response
case class DefaultResponse(status: BookingStatusEnum, reason: Option[String], reasonType: Option[Int])

sealed trait DrivePropertyType { def `type` : Int }
case object TaxiLicense extends DrivePropertyType { val `type` = 1 }
case object PrivateHireLicense extends DrivePropertyType { val `type` = 2 }
case object SharedCar extends DrivePropertyType { val `type` = 3 }
case object PrivateCar extends DrivePropertyType { val `type` = 4 }
case object ElectricCar extends DrivePropertyType { val `type` = 5 }
case object HybridCar extends DrivePropertyType { val `type` = 6 }
case object Wifi extends DrivePropertyType { val `type` = 7 }
case object WaterBottles extends DrivePropertyType { val `type` = 8 }
case object Newspapers extends DrivePropertyType { val `type` = 9 }
case object MaxPax extends DrivePropertyType { val `type` = 10 }
case object MaxLug extends DrivePropertyType { val `type` = 11 }
case object CoEmissions extends DrivePropertyType { val `type` = 12 }
case object ArrivalNotifications extends DrivePropertyType { val `type` = 13 }
case object ChildSeatType extends DrivePropertyType { val `type` = 14 }
case object AdditionalStop extends DrivePropertyType { val `type` = 15 }
case object PickupWaitTime extends DrivePropertyType { val `type` = 16 }
case object AdditionalWaitTime extends DrivePropertyType { val `type` = 17 }
case object CarMakeAndModel extends DrivePropertyType { val `type` = 18 }
case object DriverLanguages extends DrivePropertyType { val `type` = 19 }
case object TaxiSign extends DrivePropertyType { val `type` = 20 }
case object Magazines extends DrivePropertyType { val `type` = 21 }
case object DomesticWaitTime extends DrivePropertyType { val `type` = 22 }
case object InternationalWaitTime extends DrivePropertyType { val `type` = 23 }
case object ProductDescription extends DrivePropertyType { val `type` = 24 }
case object AutoAcceptAllowed extends DrivePropertyType { val `type` = 25 }
case object MobilePhoneCharger extends DrivePropertyType { val `type` = 50 }
case object SpecialDriverLanguage extends DrivePropertyType { val `type` = 51 }
case object TransferType extends DrivePropertyType { val `type` = 52 }
case object EngineType extends DrivePropertyType { val `type` = 53 }
case object ArmoredCar extends DrivePropertyType { val `type` = 54 }
case object WheelChair extends DrivePropertyType { val `type` = 55 }
case object Campaign extends DrivePropertyType { val `type` = 56 }
case object Tablet extends DrivePropertyType { val `type` = 57 }
case object SelfDrivenCar extends DrivePropertyType { val `type` = 101 }
case object FourWheelDrive extends DrivePropertyType { val `type` = 102 }
case object MaxLugPerPax extends DrivePropertyType { val `type` = 103 }
case object PickupTimeEstimated extends DrivePropertyType { val `type` = 104 }
case object MaxCabinLuggage extends DrivePropertyType { val `type` = 150 }
case object MaxSuitcaseLuggage extends DrivePropertyType { val `type` = 151 }
case object PickupLandmark extends DrivePropertyType { val `type` = 152 }
case object DropoffLandmark extends DrivePropertyType { val `type` = 153 }

sealed trait DriveType { def `type` : Int }
case object Compact extends DriveType { val `type` = 1 }
case object Sedan extends DriveType { val `type` = 2 }
case object PeopleCarrier extends DriveType { val `type` = 3 }
case object SUV extends DriveType { val `type` = 4 }
case object VanOrMinibus extends DriveType { val `type` = 5 }
case object Coach extends DriveType { val `type` = 6 }
case object StretchLimo extends DriveType { val `type` = 7 }
case object StationWagon extends DriveType { val `type` = 8 }
case object Convertible extends DriveType { val `type` = 9 }
case object SportsCar extends DriveType { val `type` = 102 }
case object Offroad extends DriveType { val `type` = 104 }
case object PickupTruck extends DriveType { val `type` = 105 }
case object Motorcycle extends DriveType { val `type` = 106 }
case object Rickshaw extends DriveType { val `type` = 107 }
case object WaterTaxi extends DriveType { val `type` = 108 }
case object Shuttle extends DriveType { val `type` = 109 }

sealed trait DriveCategoryType { def `type` : Int }
case object Economy extends DriveCategoryType { val `type` = 1 }
case object Standard extends DriveCategoryType { val `type` = 2 }
case object Business extends DriveCategoryType { val `type` = 3 }
case object Premium extends DriveCategoryType { val `type` = 4 }
case object Luxury extends DriveCategoryType { val `type` = 5 }
case object SpeediShuttle extends DriveCategoryType { val `type` = 6 }

sealed trait ServiceType { def `type` : String }
case object MeetupWithPrice extends ServiceType { val `type` = "Meetgreet" }
case object MeetupWithoutPrice extends ServiceType { val `type` = "Outdoor" }
case object AdditionalExtra extends ServiceType { val `type` = "AdditionalExtra" }

sealed trait AddressCategoryEnum { def `type` : String }
case object AddressCategory extends AddressCategoryEnum { val `type` = "address" }
case object AirportCategory extends AddressCategoryEnum { val `type` = "airport" }
package models

import play.api.libs.json.Json

case class Location(id: Option[Long] = None, name: String)
case class Address(street: String, houseNumber: Int, postalCode: String, city: String, municipality: String)

object Location  {
  implicit val locationFormat = Json.format[Location]
}

object Address {
  implicit val addressFormat = Json.format[Address]
}


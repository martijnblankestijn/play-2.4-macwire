package repo

import models.Address
import play.api.Logger
import play.api.http.HeaderNames.ACCEPT
import play.api.http.MimeTypes.JSON
import play.api.libs.json.{JsArray, JsLookupResult}

import scala.concurrent.Future

trait AddressRepository {
  def get(postalCode: String, houseNumber: Int): Future[Option[Address]]
}

import play.api.libs.ws._

import scala.concurrent.Future

/**
  * Documentation for API is http://www.postcodedata.nl/api/request/
  * @param wsClient
  */
class PostcodeDataAddressRepository(wsClient: WSClient) extends AddressRepository {
  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  private def convert(result: JsLookupResult, houseNumber: Int): Address = {
    Address(
      (result \ "street").as[String],
      houseNumber,
      (result \ "postcode").as[String],
      (result \ "city").as[String],
      (result \ "municipality").as[String]
    )

  }

  override def get(postalCode: String, houseNumber: Int) = {
    val request: Future[WSResponse] = wsClient.url("http://api.postcodedata.nl/v1/postcode/")
      .withHeaders(ACCEPT -> JSON)
      .withQueryString(
        "streetnumber" -> houseNumber.toString,
        "postcode" -> postalCode,
        "ref" -> "somewhere.nl")
      .get()

    request
      .map(r =>
        (r.json \ "status").as[String] match {
          case "no results" => None
          case "ok" => Some(convert((r.json \ "details").as[JsArray].head, houseNumber))
          case s: String =>
            Logger.info(s"Error from service $s")
            None
        }
      )
  }
}

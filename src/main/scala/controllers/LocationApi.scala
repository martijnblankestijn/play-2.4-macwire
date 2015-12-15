package controllers

import models.Location
import models.Location._
import play.api.libs.json.Json.toJson
import play.api.libs.json._
import play.api.mvc.Action._
import play.api.mvc.Controller
import repo.{AddressRepository, LocationRepository}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LocationApi(locationRepository: LocationRepository, addressRepository: AddressRepository)
  extends Controller {

  def all() = async {
    locationRepository.all().map(l => Ok(toJson(l)))
  }

  def get(id: Long) = async {
    locationRepository.get(id)
      .map(
        _.map(location => Ok(toJson(location))).getOrElse(NotFound)
      )
  }

  def delete(id: Long) = async {
    locationRepository.delete(id).map(_ => NoContent)
  }

  def deleteAll() = async {
    locationRepository.deleteAll().map(_ => NoContent)
  }

  def save = async(parse.json) { request =>
    request.body.validate[Location].fold(
      errors => Future.successful(BadRequest(JsError.toJson(errors))),
      location =>
        locationRepository
          .saveOrUpdate(location)
          .map(location => {
            val route = controllers.routes.LocationApi.get(location.id.get).url
            Created.withHeaders(LOCATION -> route)
          })
    )
  }

  def getAddress(postalCode: String, houseNumber: Int) = async {
    addressRepository.get(postalCode, houseNumber)
      .map {
        case Some(a) => Ok(Json.toJson(a))
        case None => NotFound
      }
  }
}

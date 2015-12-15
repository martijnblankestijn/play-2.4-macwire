package controllers

import models.Location
import org.specs2.mutable.Specification
import play.api.libs.json.Json
import play.api.libs.json.Json.toJson
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Future

class LocationApiTest extends Specification {

  "Location API" should {
    "return all" in new ControllerContext {

      locationRepository.all() returns Future.successful(Seq())

      val response = locationApi.all()(FakeRequest())
      status(response) must be equalTo OK

      contentAsJson(response) must be equalTo Json.arr()
    }

    "post a location" in new ControllerContext {
      val location = Location(None, "Bla")
      locationRepository.saveOrUpdate(location) returns Future.successful( location.copy(id = Some(5)))

      val request = FakeRequest().withBody(toJson(location))

      val response = locationApi.save(request)

      status(response) must be equalTo CREATED
      header(LOCATION, response) must be equalTo Some("/locations/5")
    }

    "post a location with invalid json is a bad request" in new ControllerContext {
      val request = FakeRequest().withBody(toJson("INVALID"))

      val response = locationApi.save(request)

      status(response) must be equalTo BAD_REQUEST
    }

  }
}

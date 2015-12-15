package it

import models.Location
import play.api.libs.json.Json
import play.api.test.{FakeRequest, PlaySpecification}

import scala.concurrent.ExecutionContext

class LocationApiITTest extends PlaySpecification {
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
  "locations" should {
    "return all locations" in new IntegrationContext {
      val response = route(FakeRequest("GET", "/locations"))

      response must beSome.which(status(_) == OK)
    }

    "post a new location" in new IntegrationContext {
      val postNewLocation = route(FakeRequest("POST", "/locations").withBody(Json.toJson(Location(name = "New Location"))))

      postNewLocation must beSome.which(status(_) == CREATED)

      val locationUrl = await(postNewLocation.get).header.headers(LOCATION)
      val getResponse = route(FakeRequest("GET", locationUrl))

      getResponse must beSome.which(status(_) == OK)
    }
  }
}

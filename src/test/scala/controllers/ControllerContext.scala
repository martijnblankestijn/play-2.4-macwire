package controllers

import modules.ControllerModule
import org.specs2.matcher.MustThrownExpectations
import org.specs2.mock.Mockito
import org.specs2.specification.Scope
import repo.{AddressRepository, LocationRepository}

import scala.concurrent.ExecutionContext

trait ControllerContext
  extends ControllerModule
  with MockRepositoryModule
  with Scope
  with MustThrownExpectations {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
}

trait MockRepositoryModule extends Mockito {
  lazy val locationRepository = mock[LocationRepository]
  lazy val addressRepository = mock[AddressRepository]
}

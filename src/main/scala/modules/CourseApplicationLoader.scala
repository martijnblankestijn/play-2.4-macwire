package modules


import controllers.{Assets, LocationApi}
import play.api.ApplicationLoader.Context
import play.api._
import play.api.db.evolutions.{DynamicEvolutions, EvolutionsComponents}
import play.api.db.slick.evolutions.SlickEvolutionsComponents
import play.api.db.slick.{DatabaseConfigProvider, DbName, SlickComponents}
import play.api.libs.ws.WSClient
import play.api.libs.ws.ning.NingWSComponents
import play.api.routing.Router
import repo.{AddressRepository, PostcodeDataAddressRepository, LocationRepository, SlickLocationRepository}
import router.Routes
import slick.backend.DatabaseConfig
import slick.profile.BasicProfile


class CourseApplicationLoader extends ApplicationLoader {
  def load(context: Context): Application = {
    Logger.configure(context.environment)
    new AppComponents(context).application
  }
}

// https://groups.google.com/forum/#!topic/play-framework/02WRgu_mruc
class AppComponents(context: Context)
  extends BuiltInComponentsFromContext(context)
  with NingWSComponents
  with DatabaseModule
  with RepositoryModule
  with ControllerModule {

  import com.softwaremill.macwire._

  lazy val assets: Assets = wire[Assets]
  lazy val router: Router = {
    lazy val prefix = "/"
    wire[Routes]
  }
}



trait DatabaseModule extends SlickComponents
                with EvolutionsComponents
                with SlickEvolutionsComponents {

  lazy val dbConfigProvider: DatabaseConfigProvider = new DatabaseConfigProvider {
    override def get[P <: BasicProfile]: DatabaseConfig[P] = api.dbConfig(DbName("default"))
  }
  override def dynamicEvolutions = new DynamicEvolutions


  def onStart() = {
    Logger.info("Applying evolutions")
    applicationEvolutions
  }

  onStart()
}

trait RepositoryModule {
  import com.softwaremill.macwire._

  def wsClient: WSClient
  def dbConfigProvider: DatabaseConfigProvider

  lazy val locationRepository = wire[SlickLocationRepository]
  lazy val addressRepository = wire[PostcodeDataAddressRepository]

}

trait ControllerModule {
  import com.softwaremill.macwire._

  def addressRepository : AddressRepository
  def locationRepository: LocationRepository

  lazy val locationApi = wire[LocationApi]
}
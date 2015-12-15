import modules.CourseApplicationLoader
import play.api.ApplicationLoader.createContext
import play.api.db.slick.DatabaseConfigProvider
import play.api.test.Helpers._
import play.api.{ApplicationLoader, Environment}
import repo.Locations
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._

// TODO GET IT WORKING

object SchemaGenerator {
  def main(args: Array[String]) {
    val initialSettings = Map(
      "slick.dbs.default.db.url" -> "jdbc:postgresql://192.168.99.100:5432/postgres?user=postgres&password=postgres"
    )
    val loader = new CourseApplicationLoader
    val context = createContext(environment = Environment.simple(), initialSettings = initialSettings)
    implicit val application = loader.load(context)

    running(application) {

      val databaseConfig = DatabaseConfigProvider.get[JdbcProfile]


      val locations = TableQuery[Locations]
      val schema = locations.schema
      schema.create.statements.foreach(println)

    }
  }
}

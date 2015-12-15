package repo

import models.Location
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future

trait LocationRepository {
  def get(id: Long): Future[Option[Location]]

  def all(): Future[Seq[Location]]
  def saveOrUpdate(location: Location): Future[Location]
  def delete(id: Long): Future[Int]
  def deleteAll(): Future[Int]
}

import slick.driver.PostgresDriver.api._

class SlickLocationRepository(dbConfigProvider: DatabaseConfigProvider) extends LocationRepository {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig.driver.api._

  val Locations = TableQuery[Locations]

  override def get(id: Long) = dbConfig.db.run(Locations.filter(_.id === id).result.headOption)

  override def saveOrUpdate(location: Location) = {
    val locationWithId = (Locations returning Locations.map(_.id)
                                    into ((l, id) => l.copy(id = id))
                          ) += location
    dbConfig.db.run(locationWithId)
  }

  override def all() = {
    dbConfig.db.run(Locations.result)
  }

  override def deleteAll = dbConfig.db.run(Locations.delete)

  override def delete(id: Long) = {
    val action = Locations.filter(_.id === id).delete
    dbConfig.db.run(action)
  }
}

class Locations(tag: Tag) extends Table[Location](tag, "location") {
  def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")

  def * = (id, name) <> (Location.apply _ tupled, Location.unapply)
}
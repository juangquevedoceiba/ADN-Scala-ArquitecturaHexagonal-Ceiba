package aplicacion.src.main.scala.com.incremento.servicio

import dominio.src.main.scala.com.ceiba.incremento.modelo.Incremento
import infraestructura.src.main.scala.com.ceiba.incremento.adaptador.MapeoIncremento
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc.SQLiteProfile.api._
import slick.jdbc._
import slick.lifted.TableQuery

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RepositorioIncremento @Inject() (
  protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile]{

   private lazy val incrementQuery = TableQuery[MapeoIncremento]

  def dbInit: Future[Unit] = {
    // Definición de la sentencia SQL de creación del schema
    db.run(incrementQuery.schema.createIfNotExists)
  }
  def getOne(id: String) = {
    val q = incrementQuery.filter(_.id === id)
    db.run(q.result.headOption)
  }
  def getAll = {
    val q = incrementQuery.sortBy(x => x.id)
    db.run(q.result)
  }
  def create(incremento: Incremento) = {
    val instert =incrementQuery += incremento
    db.run(instert)
      .flatMap(_ => getOne(incremento.id.getOrElse("")))
  }
  def update(id: String, incremento: Incremento) = {
    val q = incrementQuery.filter(_.id === incremento.id && incremento.id.contains(id))
    val update = q.update(incremento)
    db.run(update)
      .flatMap(_ => db.run(q.result.headOption))
  }
  def delete(id: String) = {
    val q = incrementQuery.filter(_.id === id)
    for {
      objecto <- db.run(q.result.headOption)
      _ <-db.run(q.delete)
    }yield objecto
  }
}

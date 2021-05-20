package aplicacion.src.main.scala.com.incremento.servicio

import akka.Done
import cats.data.EitherT
import dominio.src.main.scala.com.ceiba.incremento.modelo.dto.{EliminarIncrementoDTO, IncrementoDTO}
import dominio.src.main.scala.com.ceiba.incremento.repositorio.RepoIncrementoBase
import dominio.src.main.scala.com.ceiba.incremento.servicio.CalcularMontoFinal
import infraestructura.src.main.scala.com.ceiba.incremento.MensajeError
import infraestructura.src.main.scala.com.ceiba.incremento.adaptador.dao.MapeoIncremento
import monix.eval.Task
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc.SQLiteProfile.api._
import slick.jdbc._
import slick.lifted.TableQuery

import java.time.LocalDate
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

class RepositorioIncremento @Inject() (
  protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents, calcularMontoFinal: CalcularMontoFinal)(implicit ec: ExecutionContext)
  extends RepoIncrementoBase with HasDatabaseConfigProvider[JdbcProfile]{

   private lazy val incrementQuery = TableQuery[MapeoIncremento]


  def dbInit: Future[Unit] = {
    // Definición de la sentencia SQL de creación del schema
    db.run(incrementQuery.schema.createIfNotExists)
  }

  def consultarIncremento(id: Int) =  {
    val consultaIncremento = incrementQuery.filter(_.id === id)
    db.run(consultaIncremento.result.headOption)
  }

  def consultarIncrementos() =  {
    val consultaIncrementos =incrementQuery.sortBy(x => x.id)
    db.run(consultaIncrementos.result)
  }

  def crearIncremento(incremento: IncrementoDTO):Future[Int] =  {
    val creaIncremento = incrementQuery += incremento
    db.run(creaIncremento)
  }

  def eliminarIncremento(idIncremento: EliminarIncrementoDTO) =  {
      val eliminarIncremento = incrementQuery.filter(_.id === idIncremento.id).delete
      db.run(eliminarIncremento)
  }

   def actualizarIncremento(incremento: IncrementoDTO) = {
    val actualizar = incrementQuery.filter(_.id === incremento.id)
      .map(tabla => (tabla.fechaInicio, tabla.fechaFin, tabla.montoInicial))
      .update((incremento.fechaInicio, incremento.fechaFin, incremento.montoInicial))
     db.run(actualizar)
   }

}



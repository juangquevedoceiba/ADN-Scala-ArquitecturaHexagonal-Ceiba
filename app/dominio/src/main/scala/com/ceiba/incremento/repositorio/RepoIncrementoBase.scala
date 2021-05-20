package dominio.src.main.scala.com.ceiba.incremento.repositorio

import akka.Done
import cats.data.EitherT
import dominio.src.main.scala.com.ceiba.incremento.excepcion.MensajeError
import dominio.src.main.scala.com.ceiba.incremento.modelo.dto.{EliminarIncrementoDTO, IncrementoDTO}
import monix.eval.Task

import scala.concurrent.Future

trait RepoIncrementoBase {
  def eliminarIncremento(idIncremento: EliminarIncrementoDTO):Future[Int]

  def consultarIncrementos():Future[Seq[IncrementoDTO]]

  def consultarIncremento(id: Int): Future[Option[IncrementoDTO]]

  def crearIncremento(incremento: IncrementoDTO): Future[Int]

  def actualizarIncremento(incremento: IncrementoDTO): Future[Int]
}

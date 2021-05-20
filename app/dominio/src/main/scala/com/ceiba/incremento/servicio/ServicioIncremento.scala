package dominio.src.main.scala.com.ceiba.incremento.servicio

import akka.Done
import aplicacion.src.main.scala.com.incremento.servicio.{Constantes, RepositorioIncremento}
import dominio.src.main.scala.com.ceiba.incremento.modelo.dto.{EliminarIncrementoDTO, IncrementoDTO}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc.JdbcProfile
import dominio.src.main.scala.com.ceiba.incremento.excepcion.ValidarArgumento
import infraestructura.src.main.scala.com.ceiba.incremento.MensajeError

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ServicioIncremento @Inject() (
                                     protected val dbConfigProvider: DatabaseConfigProvider,
                                     cc: ControllerComponents, repositorioIncremento: RepositorioIncremento, calcularMontoFinal: CalcularMontoFinal
      )(implicit ec: ExecutionContext)
  extends AbstractController (cc) with HasDatabaseConfigProvider[JdbcProfile]{


  def eliminarIncremento(idIncremento: EliminarIncrementoDTO): Future[Int] = repositorioIncremento.eliminarIncremento(idIncremento)
  def consultarIncrementos(): Future[Seq[IncrementoDTO]] = repositorioIncremento.consultarIncrementos()
  def consultarIncremento(id: Int): Future[Option[IncrementoDTO]] = repositorioIncremento.consultarIncremento(id)

  def crearIncremento(incremento: IncrementoDTO): Future[Int]= {
    validar(incremento:IncrementoDTO)
        .fold(
        error => Future.failed(new Exception(error.mensaje)),
        _ => {
          val calculoMontoFinal= calcularMontoFinal.calcular(incremento.fechaInicio,incremento.fechaFin, incremento.montoInicial)
          val incrementoModificado = incremento.copy(montoFinal = calculoMontoFinal)
          repositorioIncremento.crearIncremento(incrementoModificado)
        }
      )
  }

  def actualizarIncremento(incremento:IncrementoDTO): Future[Int] = {
    validar(incremento:IncrementoDTO)
      .fold(
        error => Future.failed(new Exception(error.mensaje)),
        _ => {
          val calculoMontoFinal = calcularMontoFinal.calcular(incremento.fechaInicio, incremento.fechaFin, incremento.montoInicial)
          val incrementoModificado = incremento.copy(montoFinal = calculoMontoFinal)
          repositorioIncremento.actualizarIncremento(incrementoModificado)
        }
      )
  }

  def validar(incremento: IncrementoDTO)={
    for{
      _ <- ValidarArgumento.validarFechaCorrecta(incremento.fechaInicio, Constantes.LA_FECHA_DE_INICIO_ES_INVALIDA)
      _ <- ValidarArgumento.validarFechaCorrecta(incremento.fechaFin, Constantes.LA_FECHA_FINAL_ES_INVALIDA)
      _ <- ValidarArgumento.validarLapsoTiempo(incremento.fechaInicio, incremento.fechaFin, Constantes.CANTIDAD_MAXIMA_DIAS, Constantes.LAPSO_DE_TIEMPO_MENOR_QUINCE_DIAS)
      _ <- ValidarArgumento.validarMenor(incremento.fechaInicio, incremento.fechaFin, Constantes.LA_FECHA_FIN_ES_DESPUES_DE_LA_FECHA_INICIO)
      _ <- ValidarArgumento.validarTopeMaximo(incremento.montoInicial, Constantes.MONTO_MAXIMO, Constantes.VALOR_MONTO_MAXIMO)
      _ <- ValidarArgumento.validarTopeMinimo(incremento.montoInicial, Constantes.MONTO_MINIMO, Constantes.VALOR_MONTO_MINIMO)
    } yield Done
  }


}

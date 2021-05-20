package infraestructura.src.main.scala.com.ceiba.incremento.controlador
import dominio.src.main.scala.com.ceiba.incremento.modelo.dto.{EliminarIncrementoDTO, IncrementoDTO}
import dominio.src.main.scala.com.ceiba.incremento.servicio.ServicioIncremento
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class ComandoControladorIncremento @Inject()(
      cc: ControllerComponents,
      servicioIncremento: ServicioIncremento
      ) extends AbstractController(cc) {

  implicit val serializador = Json.format[IncrementoDTO]
  implicit val serializar = Json.format[EliminarIncrementoDTO]

  val logger = play.Logger.of("ComandoControladorIncremento")

  def creaIncremento = Action.async(parse.json){ request =>
    val validador = request.body.validate[IncrementoDTO]
    validador.asEither match {
      case Left (error) => Future.successful(BadRequest(error.toString()))
      case Right(incremento) => {
            servicioIncremento.crearIncremento(incremento)
          .map( _ => {
            val j = Json.obj("data"->incremento , "message" -> "Incremento creado" )
            Ok(j)
          }).recover{
          case ex =>
            logger.error("Ocurrio un error al crear el Incremento",ex)
            InternalServerError(s"Error ${ex.getLocalizedMessage}")
        }
      }
    }
  }
  def actualizaIncremento(id: Int) =  Action.async(parse.json) { request =>
    val validador = request.body.validate[IncrementoDTO]
    val logger = play.Logger.of("ComandoControladorIncremento")
    validador.asEither match {
      case Left(error) => Future.successful(BadRequest(error.toString()))
      case Right(incremento) => {
        servicioIncremento
          .actualizarIncremento(incremento)
          .map(incremento => {
            val j = Json.obj("data" -> incremento, "message" -> "Incrementos Listados")
            Ok(j)
          }).recover {
          case ex =>
            logger.error("Ocurrio un error al actualizar el Incremento", ex)
            InternalServerError(s"Error ${ex.getLocalizedMessage}")
        }
      }
    }
  }


  def eliminaIncremento = Action.async(parse.json){ request =>
    val validador = request.body.validate[EliminarIncrementoDTO]
    validador.asEither match {
      case Left(error) => Future.successful(BadRequest(error.toString()))
      case Right(idIncremento) => {
        servicioIncremento.eliminarIncremento(idIncremento)
          .map(_ => {
            val j = Json.obj("data" -> idIncremento, "message" -> " Incremento Eliminado")
            Ok(j)
          }).recover {
          case ex =>
            logger.error("Ocurrio un error al eliminar el Incremento", ex)
            InternalServerError(s"Error ${ex.getMessage}")
        }
      }
    }
  }

}

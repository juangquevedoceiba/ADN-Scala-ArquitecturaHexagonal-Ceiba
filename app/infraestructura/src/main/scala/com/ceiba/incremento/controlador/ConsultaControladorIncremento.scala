package infraestructura.src.main.scala.com.ceiba.incremento.controlador

import aplicacion.src.main.scala.com.incremento.servicio.RepositorioIncremento
import dominio.src.main.scala.com.ceiba.incremento.modelo.dto.IncrementoDTO
import dominio.src.main.scala.com.ceiba.incremento.servicio.ServicioIncremento
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ConsultaControladorIncremento @Inject()(
      cc: ControllerComponents,
      servicioIncremento: ServicioIncremento
      ) extends AbstractController(cc) {

  implicit val serializador = Json.format[IncrementoDTO]
  val logger = play.Logger.of("ConsultaControladorIncremento")

  def consultaIncrementos = Action.async{
    servicioIncremento
      .consultarIncrementos()
      .map( incrementos => {
        val j = Json.obj("data"->incrementos, "message" -> "Incrementos Listados"  )
        Ok(j)
      }).recover{
      case ex =>
        logger.error("Ocurrio un error al consultar los Incrementos",ex)
        InternalServerError(s"Error ${ex.getLocalizedMessage}")
    }
  }
  def consultarIncremento(id: Int) = Action.async{
        servicioIncremento
          .consultarIncremento(id)
      .map( incremento => {
        val j = Json.obj("data"->incremento, "message" -> "Incrementos Listados"  )
        Ok(j)
      }).recover{
      case ex =>
        logger.error("Ocurrio un error al consultar el Incremento",ex)
        InternalServerError(s"Error ${ex.getLocalizedMessage}")
    }
  }


}

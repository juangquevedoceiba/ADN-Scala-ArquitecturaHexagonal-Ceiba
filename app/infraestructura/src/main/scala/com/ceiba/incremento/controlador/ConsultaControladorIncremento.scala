package infraestructura.src.main.scala.com.ceiba.incremento.controlador

import aplicacion.src.main.scala.com.incremento.servicio.RepositorioIncremento
import dominio.src.main.scala.com.ceiba.incremento.modelo.Incremento
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ConsultaControladorIncremento @Inject()(
      cc: ControllerComponents,
      repositorioIncremento: RepositorioIncremento
      ) extends AbstractController(cc) {

  implicit val serializador = Json.format[Incremento]
  val logger = play.Logger.of("ComandoControladorIncremento")

  def getIncrementos = Action.async{
    repositorioIncremento
      .getAll
      .map( incrementos => {
        val j = Json.obj("data"->incrementos, "message" -> "Movies Listed"  )
        Ok(j)
      }).recover{
      case ex =>
        logger.error("An error ocurred getMovies",ex)
        InternalServerError(s"Error ${ex.getLocalizedMessage}")
    }
  }
  def getIncremento(id: String) = Action.async{
    repositorioIncremento
      .getOne(id)
      .map( incremento => {
        val j = Json.obj("data"->incremento, "message" -> "Incrementos Listados"  )
        Ok(j)
      }).recover{
      case ex =>
        logger.error("An error ocurred getIncremento",ex)
        InternalServerError(s"Error ${ex.getLocalizedMessage}")
    }

  }
}

package infraestructura.src.main.scala.com.ceiba.incremento.controlador
import aplicacion.src.main.scala.com.incremento.servicio.RepositorioIncremento
import dominio.src.main.scala.com.ceiba.incremento.modelo.Incremento
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ComandoControladorIncremento @Inject()(
      cc: ControllerComponents,
      repositorioIncremento: RepositorioIncremento
      ) extends AbstractController(cc) {

  implicit val serializador = Json.format[Incremento]
  val logger = play.Logger.of("ComandoControladorIncremento")

  def creaIncremento = Action.async(parse.json){ request =>
    val validador = request.body.validate[Incremento]
    validador.asEither match {
      case Left (error) => Future.successful(BadRequest(error.toString()))
      case Right(incremento) => {
        repositorioIncremento
          .create(incremento)
          .map( incremento => {
            val j = Json.obj("data"->incremento , "message" -> "Incremento createdo" )
            Ok(j)
          }).recover{
          case ex =>
            logger.error("An error ocurred createMovie",ex)
            InternalServerError(s"Error ${ex.getLocalizedMessage}")
        }
      }
    }
  }
  def actualizaIncremento(id: String) =  Action.async(parse.json) { request =>
    val validador = request.body.validate[Incremento]
    val logger = play.Logger.of("MovieController")
    validador.asEither match {
      case Left(error) => Future.successful(BadRequest(error.toString()))
      case Right(incremento) => {
        repositorioIncremento
          .update(id, incremento)
          .map(movie => {
            val j = Json.obj("data" -> incremento, "message" -> "Movie updated")
            Ok(j)
          }).recover {
          case ex =>
            logger.error("An error ocurred updateMovie", ex)
            InternalServerError(s"Error ${ex.getLocalizedMessage}")
        }
      }
    }
  }
  def eliminaIncremento(id: String) = Action.async{
    repositorioIncremento
      .delete(id)
      .map( incremento => {
        val j = Json.obj("data"->incremento, "message" -> "Movie Listed"  )
        Ok(j)
      }).recover{
      case ex =>
        logger.error("An error ocurred deleteMovie",ex)
        InternalServerError(s"Error ${ex.getLocalizedMessage}")
    }
  }
}

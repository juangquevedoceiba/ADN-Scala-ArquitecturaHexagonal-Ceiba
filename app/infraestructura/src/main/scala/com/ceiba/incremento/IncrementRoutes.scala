package infraestructura.src.main.scala.com.ceiba.incremento

import infraestructura.src.main.scala.com.ceiba.incremento.controlador.{ComandoControladorIncremento, ConsultaControladorIncremento}
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

import javax.inject.Inject

class IncrementRoutes @Inject()( consulta:ConsultaControladorIncremento, comando: ComandoControladorIncremento) extends SimpleRouter {
  override def routes: Routes = {
    case GET(p"/") => consulta.getIncrementos
    case GET(p"/$id") => consulta.getIncremento(id: String)
    case POST(p"/") => comando.creaIncremento
    case PATCH(p"/$id") => comando.actualizaIncremento(id: String)
    case DELETE(p"/$id") => comando.eliminaIncremento(id: String)
  }
}

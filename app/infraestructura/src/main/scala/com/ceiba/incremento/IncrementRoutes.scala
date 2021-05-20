package infraestructura.src.main.scala.com.ceiba.incremento

import infraestructura.src.main.scala.com.ceiba.incremento.controlador.{ComandoControladorIncremento, ConsultaControladorIncremento}
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

import javax.inject.Inject

class IncrementRoutes @Inject()( consulta :ConsultaControladorIncremento, comando: ComandoControladorIncremento) extends SimpleRouter {
  override def routes: Routes = {
    case GET(p"/") => consulta.consultaIncrementos
    case GET(p"/${int(id)}") => consulta.consultarIncremento(id: Int)
    case POST(p"/") => comando.creaIncremento
    case POST(p"/${int(id)}") => comando.actualizaIncremento(id: Int)
    case DELETE(p"/") => comando.eliminaIncremento()
  }
}

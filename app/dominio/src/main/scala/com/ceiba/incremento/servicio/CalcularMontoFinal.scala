package dominio.src.main.scala.com.ceiba.incremento.servicio

import aplicacion.src.main.scala.com.incremento.servicio.Constantes
import infraestructura.src.main.scala.com.ceiba.incremento.adaptador.repositorio.RepositorioDiasFestivosHolyday
import play.api.mvc.{AbstractController, ControllerComponents}

import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS
import javax.inject.Inject
class CalcularMontoFinal @Inject()(
                         cc: ControllerComponents,
                         repoFestivo: RepositorioDiasFestivosHolyday,
                         ) extends AbstractController(cc) {



  def calcular(fechaInicial:String, fechaFinal:String, montoInicial: Double): Double ={
    val diasLapso = DAYS.between(LocalDate.parse(fechaInicial), LocalDate.parse(fechaFinal)).toInt
    val diasFinchos = contarDiasFinSemana(LocalDate.parse(fechaInicial), diasLapso)
    val diasFestivos = repoFestivo.contarFestivos(LocalDate.parse(fechaInicial), diasLapso)
    montoInicial * (Math.pow(1 + Constantes.INTERES, diasLapso - diasFinchos - diasFestivos) + Math.pow(1 + Constantes.INTERES_FIN_DE_SEMANA, diasFinchos - diasFestivos))
  }

  def contarDiasFinSemana(fechaInicial:LocalDate, diasLapso:Int): Int ={
    var contadorFinchos: Int = 0
    for(dia <-0 to diasLapso){
      val fechaAValidar = fechaInicial.plusDays(dia)
      if (fechaAValidar.getDayOfWeek.getValue == Constantes.SABADO || fechaAValidar.getDayOfWeek.getValue == Constantes.DOMINGO)
          contadorFinchos += 1
    }
    contadorFinchos
  }
}

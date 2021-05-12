package dominio.src.main.scala.com.ceiba.incremento.servicio

import infraestructura.src.main.scala.com.ceiba.incremento.adaptador.RepositorioDiasFestivosHolyday
import play.api.mvc.{AbstractController, ControllerComponents}

import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS
import javax.inject.Inject
class CalcularMontoFinal @Inject()(
                         cc: ControllerComponents,
                         repoFestivo: RepositorioDiasFestivosHolyday,
                         ) extends AbstractController(cc) {

  private val SABADO = 6
  private val DOMINGO = 7
  private val INTERES = 0.05
  private val INTERES_FIN_DE_SEMANA = 0.015

  def calcular(fechaInicial:LocalDate, fechaFinal:LocalDate, montoInicial: Double): Double ={
    val diasLapso = DAYS.between(fechaInicial, fechaFinal).toInt
    val diasFinchos = contarDiasFinSemana(fechaInicial, diasLapso)
    val diasFestivos = repoFestivo.contarFestivos(fechaInicial, diasLapso)
    montoInicial * (Math.pow(1 + INTERES, diasLapso - diasFinchos - diasFestivos) + Math.pow(1 + INTERES_FIN_DE_SEMANA, diasFinchos - diasFestivos))
  }

  def contarDiasFinSemana(fechaInicial:LocalDate, diasLapso:Int): Int ={
    var contadorFinchos: Int = 0
    for(dia <-0 to diasLapso){
      val fechaAValidar = fechaInicial.plusDays(dia)
      if (fechaAValidar.getDayOfWeek.getValue == SABADO || fechaAValidar.getDayOfWeek.getValue == DOMINGO)
          contadorFinchos += 1
    }
    contadorFinchos
  }
}

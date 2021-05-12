package infraestructura.src.main.scala.com.ceiba.incremento.adaptador

import java.time.LocalDate


class RepositorioDiasFestivosHolyday {
    def contarFestivos(fechaInicial:LocalDate, diasLapso:Int): Int ={
      var contadorFestivos = 0
      for(dia <-0 to diasLapso){
        val fechaAValidar = fechaInicial.plusDays(dia)
        val diasFestivos = new HolidayUtil(fechaAValidar.getYear)
        val esFestivo = diasFestivos.isHoliday(fechaAValidar.getMonthValue, fechaAValidar.getDayOfMonth)
        if (esFestivo)
          contadorFestivos += 1
        }
      contadorFestivos
    }
}

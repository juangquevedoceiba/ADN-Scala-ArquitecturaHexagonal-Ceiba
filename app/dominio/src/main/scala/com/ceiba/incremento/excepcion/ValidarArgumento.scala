package dominio.src.main.scala.com.ceiba.incremento.excepcion

import akka.Done
import aplicacion.src.main.scala.com.incremento.servicio.Constantes
import play.api.mvc.{Action, AnyContent}

import java.text.{ParseException, SimpleDateFormat}
import java.time.LocalDate
import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.time.temporal.ChronoUnit.DAYS


object ValidarArgumento {

  def validarLapsoTiempo(fechaInicial: String, fechaFinal: String, cantidadMaximaDias: Int, mensaje: String): Either[MensajeError, Done]  = {
    val diasLapso = DAYS.between(LocalDate.parse(fechaInicial), LocalDate.parse(fechaFinal)).toInt
    if (diasLapso > cantidadMaximaDias) {
      Left(MensajeError(mensaje, 500))
    } else {
      Right(Done)
    }
  }

  def validarFechaCorrecta(valor: String, mensaje: String): Either[MensajeError, Done] = {
    try {
      val formatoFecha = new SimpleDateFormat(Constantes.FORMATO_FECHA)
      formatoFecha.setLenient(false)
      formatoFecha.parse(valor)
      val formatter = DateTimeFormatter.ofPattern(Constantes.FORMATO_FECHA)
      LocalDate.parse(valor, formatter)
      Right(Done)
    } catch {
      case _: ParseException | _: DateTimeParseException =>
      Left(MensajeError(mensaje, 500))
    }
  }

  def validarTopeMaximo(valor: Double, topeMaximo: Int, mensaje: String): Either[MensajeError, Done] = {
    if (valor > topeMaximo) {
      Left(MensajeError(mensaje, 500))
    } else {
      Right(Done)
    }
  }

  def validarTopeMinimo(valor: Double, topeMinimo: Int, mensaje: String): Either[MensajeError, Done] = {
    if (valor < topeMinimo) {
      Left(MensajeError(mensaje, 500))
    } else {
      Right(Done)
    }
  }

  def validarMenor(fechaInicial: String, fechaFinal: String, mensaje: String): Either[MensajeError, Done] = {
    if (LocalDate.parse(fechaInicial).isAfter(LocalDate.parse(fechaFinal))) {
      Left(MensajeError(mensaje, 500))
    }else {
      Right(Done)
    }
  }

}

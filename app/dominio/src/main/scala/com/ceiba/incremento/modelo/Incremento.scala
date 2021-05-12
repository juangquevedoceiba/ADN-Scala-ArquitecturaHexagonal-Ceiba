package dominio.src.main.scala.com.ceiba.incremento.modelo

import java.util.UUID

case class Incremento(
    id: Option[String] = Option(UUID.randomUUID.toString),
    fechaInicio: String,
    fechaFin: String,
    montoInicial:Double,
    montoFinal: Double
 )

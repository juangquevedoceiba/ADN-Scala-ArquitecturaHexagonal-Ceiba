package infraestructura.src.main.scala.com.ceiba.incremento.adaptador

import dominio.src.main.scala.com.ceiba.incremento.modelo.Incremento
import slick.jdbc.SQLiteProfile.api._

class MapeoIncremento(tag : Tag) extends Table[Incremento](tag, "TEMP_INCREMENTO"){
  def id = column[String]("id", O.PrimaryKey)
  def fechaInicio = column[String]("fechaInicio")
  def fechaFin = column[String]("fechaFin")
  def montoInicial = column[Double]("montoInicial")
  def montoFinal = column[Double]("montoFinal")
  def * =
    (id.?, fechaInicio, fechaFin, montoInicial, montoFinal) <> (Incremento.tupled, Incremento.unapply)

}


package infraestructura.src.main.scala.com.ceiba.incremento.adaptador.dao

import dominio.src.main.scala.com.ceiba.incremento.modelo.dto.IncrementoDTO
import slick.jdbc.SQLiteProfile.api._

class MapeoIncremento(tag : Tag) extends Table[IncrementoDTO](tag, "TEMP_INCREMENTO"){
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def fechaInicio = column[String]("fechaInicio")
  def fechaFin = column[String]("fechaFin")
  def montoInicial = column[Double]("montoInicial")
  def montoFinal = column[Double]("montoFinal")
  def * =
    (id, fechaInicio, fechaFin, montoInicial, montoFinal) <> (IncrementoDTO.tupled, IncrementoDTO.unapply)

}



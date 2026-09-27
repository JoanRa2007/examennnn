package pe.edu.upeu.andinasaludra.domain.model

data class Reprogramacion(
    val fechaAnterior: Fecha,
    val horaAnterior: Hora,
    val fechaNueva: Fecha,
    val horaNueva: Hora
)
package pe.edu.upeu.andinasaludra.domain.time

import pe.edu.upeu.andinasaludra.domain.model.FechaHora

interface Reloj {
    fun ahora(): FechaHora
}

expect class RelojSistema() : Reloj {
    override fun ahora(): FechaHora
}
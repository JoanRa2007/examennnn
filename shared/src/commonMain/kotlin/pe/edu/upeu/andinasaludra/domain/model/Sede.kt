package pe.edu.upeu.andinasaludra.domain.model

data class Sede(val id: String, val nombre: String) { init { require(nombre.isNotBlank()) } }

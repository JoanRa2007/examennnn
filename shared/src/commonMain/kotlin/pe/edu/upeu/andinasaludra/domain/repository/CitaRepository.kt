package pe.edu.upeu.andinasaludra.domain.repository

import pe.edu.upeu.andinasaludra.domain.model.*

interface CitaRepository {
    suspend fun obtenerPaciente(): Paciente
    suspend fun obtenerCatalogo(): CatalogoCitas
    suspend fun obtenerCitas(): List<Cita>
    suspend fun obtenerCita(id: Long): Cita?
    suspend fun guardarCita(cita: Cita): Cita
    suspend fun actualizarCita(cita: Cita): Cita
}

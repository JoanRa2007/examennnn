package pe.edu.upeu.andinasaludra.domain.usecase

import pe.edu.upeu.andinasaludra.domain.model.EstadoCita
import pe.edu.upeu.andinasaludra.domain.repository.CitaRepository
import pe.edu.upeu.andinasaludra.domain.time.Reloj

class CancelarCitaUseCase(private val repository: CitaRepository, private val reloj: Reloj) {
    suspend operator fun invoke(id: Long): Result<Unit> = runCatching {
        val cita = repository.obtenerCita(id) ?: error("No se encontró la cita")
        require(ReglasCita.puedeCancelar(cita, reloj.ahora())) {
            "Solo puedes cancelar una cita programada con más de 24 horas de anticipación"
        }
        repository.actualizarCita(
            cita.copy(estado = EstadoCita.Cancelada("Cancelada por el paciente", true))
        )
        Unit
    }
}
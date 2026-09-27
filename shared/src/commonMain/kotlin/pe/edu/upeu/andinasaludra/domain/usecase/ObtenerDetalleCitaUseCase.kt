package pe.edu.upeu.andinasaludra.domain.usecase

import pe.edu.upeu.andinasaludra.domain.model.Cita
import pe.edu.upeu.andinasaludra.domain.repository.CitaRepository

class ObtenerDetalleCitaUseCase(private val repository: CitaRepository) {
    suspend operator fun invoke(id: Long): Result<Cita> = runCatching {
        repository.obtenerCita(id) ?: error("No se encontró la cita")
    }
}

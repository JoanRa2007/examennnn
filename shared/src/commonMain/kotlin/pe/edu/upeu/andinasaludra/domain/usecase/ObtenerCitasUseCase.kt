package pe.edu.upeu.andinasaludra.domain.usecase

import pe.edu.upeu.andinasaludra.domain.model.Cita
import pe.edu.upeu.andinasaludra.domain.repository.CitaRepository

class ObtenerCitasUseCase(private val repository: CitaRepository) {
    suspend operator fun invoke(): Result<List<Cita>> = runCatching {
        repository.obtenerCitas().sortedWith(compareBy<Cita> { it.fecha }.thenBy { it.hora })
    }
}

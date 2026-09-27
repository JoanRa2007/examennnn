package pe.edu.upeu.andinasaludra.domain.usecase

import pe.edu.upeu.andinasaludra.domain.model.Paciente
import pe.edu.upeu.andinasaludra.domain.repository.CitaRepository

class ObtenerPacienteUseCase(private val repository: CitaRepository) {
    suspend operator fun invoke(): Result<Paciente> = runCatching { repository.obtenerPaciente() }
}

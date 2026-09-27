package pe.edu.upeu.andinasaludra.domain.usecase

import pe.edu.upeu.andinasaludra.domain.model.CatalogoCitas
import pe.edu.upeu.andinasaludra.domain.repository.CitaRepository

class ObtenerCatalogoUseCase(private val repository: CitaRepository) {
    suspend operator fun invoke(): Result<CatalogoCitas> = runCatching { repository.obtenerCatalogo() }
}

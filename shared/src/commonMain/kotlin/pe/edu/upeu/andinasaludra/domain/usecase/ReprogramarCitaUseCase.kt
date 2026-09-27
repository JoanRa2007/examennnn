package pe.edu.upeu.andinasaludra.domain.usecase

import pe.edu.upeu.andinasaludra.domain.model.Cita
import pe.edu.upeu.andinasaludra.domain.model.EstadoCita
import pe.edu.upeu.andinasaludra.domain.model.Reprogramacion
import pe.edu.upeu.andinasaludra.domain.repository.CitaRepository
import pe.edu.upeu.andinasaludra.domain.time.Reloj

data class ErroresReprogramacion(
    val fecha: String? = null,
    val hora: String? = null,
    val general: String? = null
) {
    val hayErrores: Boolean
        get() =
            fecha != null ||
                    hora != null ||
                    general != null
}

class ReprogramacionInvalidaException(
    val errores: ErroresReprogramacion
) : Exception(
    "Reprogramación inválida"
)

class ReprogramarCitaUseCase(
    private val repository: CitaRepository,
    private val reloj: Reloj
) {

    suspend operator fun invoke(
        citaId: Long,
        fechaTexto: String,
        horaTexto: String
    ): Result<Cita> = runCatching {

        val cita =
            repository.obtenerCita(citaId)
                ?: throw ReprogramacionInvalidaException(
                    ErroresReprogramacion(
                        general =
                            "La cita no existe"
                    )
                )

        if (cita.estado !is EstadoCita.Programada) {
            throw ReprogramacionInvalidaException(
                ErroresReprogramacion(
                    general =
                        "Solo se pueden reprogramar citas programadas"
                )
            )
        }

        val citas =
            repository.obtenerCitas()

        val validacion =
            ReglasCita.validarProgramacion(
                fechaTexto = fechaTexto,
                horaTexto = horaTexto,
                ahora = reloj.ahora(),
                citas = citas,
                ignorarCitaId = cita.id
            )

        val errores =
            ErroresReprogramacion(
                fecha =
                    validacion.errorFecha,
                hora =
                    validacion.errorHora
            )

        if (errores.hayErrores) {
            throw ReprogramacionInvalidaException(
                errores
            )
        }

        val nuevaFecha =
            validacion.fecha!!

        val nuevaHora =
            validacion.hora!!

        if (
            cita.fecha == nuevaFecha &&
            cita.hora == nuevaHora
        ) {
            throw ReprogramacionInvalidaException(
                ErroresReprogramacion(
                    general =
                        "Selecciona una fecha u hora diferente a la actual"
                )
            )
        }

        val cambio =
            Reprogramacion(
                fechaAnterior =
                    cita.fecha,
                horaAnterior =
                    cita.hora,
                fechaNueva =
                    nuevaFecha,
                horaNueva =
                    nuevaHora
            )

        val citaActualizada =
            cita.copy(
                fecha = nuevaFecha,
                hora = nuevaHora,
                reprogramaciones =
                    cita.reprogramaciones +
                            cambio
            )

        repository.actualizarCita(
            citaActualizada
        )
    }
}
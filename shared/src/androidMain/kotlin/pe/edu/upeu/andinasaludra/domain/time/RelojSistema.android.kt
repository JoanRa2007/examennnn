package pe.edu.upeu.andinasaludra.domain.time

import pe.edu.upeu.andinasaludra.domain.model.Fecha
import pe.edu.upeu.andinasaludra.domain.model.FechaHora
import pe.edu.upeu.andinasaludra.domain.model.Hora
import java.util.Calendar

actual class RelojSistema actual constructor() : Reloj {
    actual override fun ahora(): FechaHora {
        val cal = Calendar.getInstance()
        return FechaHora(
            fecha = Fecha(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH)
            ),
            hora = Hora(
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE)
            )
        )
    }
}

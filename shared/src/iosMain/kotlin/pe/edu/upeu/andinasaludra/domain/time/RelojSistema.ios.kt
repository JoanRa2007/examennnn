package pe.edu.upeu.andinasaludra.domain.time

import kotlinx.cinterop.ExperimentalForeignApi
import pe.edu.upeu.andinasaludra.domain.model.Fecha
import pe.edu.upeu.andinasaludra.domain.model.FechaHora
import pe.edu.upeu.andinasaludra.domain.model.Hora
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate

@OptIn(ExperimentalForeignApi::class)
actual class RelojSistema actual constructor() : Reloj {
    actual override fun ahora(): FechaHora {
        val calendario = NSCalendar.currentCalendar
        val componentes = calendario.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay or
                NSCalendarUnitHour or NSCalendarUnitMinute,
            fromDate = NSDate()
        )
        return FechaHora(
            fecha = Fecha(
                componentes.year.toInt(),
                componentes.month.toInt(),
                componentes.day.toInt()
            ),
            hora = Hora(
                componentes.hour.toInt(),
                componentes.minute.toInt()
            )
        )
    }
}

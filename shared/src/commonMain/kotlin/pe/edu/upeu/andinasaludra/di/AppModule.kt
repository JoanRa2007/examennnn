package pe.edu.upeu.andinasaludra.di

import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import pe.edu.upeu.andinasaludra.data.repository.CitaRepositoryFake
import pe.edu.upeu.andinasaludra.domain.repository.CitaRepository
import pe.edu.upeu.andinasaludra.domain.time.Reloj
import pe.edu.upeu.andinasaludra.domain.time.RelojSistema
import pe.edu.upeu.andinasaludra.domain.usecase.CancelarCitaUseCase
import pe.edu.upeu.andinasaludra.domain.usecase.ObtenerCatalogoUseCase
import pe.edu.upeu.andinasaludra.domain.usecase.ObtenerCitasUseCase
import pe.edu.upeu.andinasaludra.domain.usecase.ObtenerDetalleCitaUseCase
import pe.edu.upeu.andinasaludra.domain.usecase.ObtenerPacienteUseCase
import pe.edu.upeu.andinasaludra.domain.usecase.ReprogramarCitaUseCase
import pe.edu.upeu.andinasaludra.domain.usecase.SolicitarCitaUseCase
import pe.edu.upeu.andinasaludra.presentation.citas.CitasViewModel
import pe.edu.upeu.andinasaludra.presentation.detalle.DetalleCitaViewModel
import pe.edu.upeu.andinasaludra.presentation.inicio.InicioViewModel
import pe.edu.upeu.andinasaludra.presentation.perfil.PerfilViewModel
import pe.edu.upeu.andinasaludra.presentation.solicitud.SolicitudViewModel

val dataModule = module {
    single<Reloj> { RelojSistema() }
    single<CitaRepository> { CitaRepositoryFake(get()) }
}

val domainModule = module {
    factory { ObtenerCitasUseCase(get()) }
    factory { ObtenerDetalleCitaUseCase(get()) }
    factory { ObtenerPacienteUseCase(get()) }
    factory { ObtenerCatalogoUseCase(get()) }
    factory { SolicitarCitaUseCase(get(), get()) }
    factory { CancelarCitaUseCase(get(), get()) }
    factory { ReprogramarCitaUseCase(get(), get()) }
}

val presentationModule = module {
    viewModel { InicioViewModel(obtenerPaciente = get(), obtenerCitas = get()) }
    viewModel { CitasViewModel(obtenerCitas = get(), reloj = get()) }
    viewModel { DetalleCitaViewModel(obtener = get(), cancelar = get(), reprogramar = get()) }
    viewModel { SolicitudViewModel(obtenerCatalogo = get(), solicitar = get()) }
    viewModel { PerfilViewModel(obtener = get()) }
}

expect val platformModule: Module

fun initKoin(
    configuracionAdicional: KoinApplication.() -> Unit = {}
) {
    // Si Koin ya fue iniciado antes (por ejemplo si initKoin() se llama
    // dos veces), no lo volvemos a iniciar para evitar un crash.
    if (GlobalContext.getOrNull() != null) return

    startKoin {
        configuracionAdicional()
        modules(
            dataModule,
            domainModule,
            presentationModule,
            platformModule
        )
    }
}
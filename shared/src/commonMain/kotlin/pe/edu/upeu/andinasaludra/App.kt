package pe.edu.upeu.andinasaludra

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
// ¡CERO IMPORTACIONES DE ÍCONOS AQUÍ!
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import pe.edu.upeu.andinasaludra.presentation.citas.CitasScreen
import pe.edu.upeu.andinasaludra.presentation.citas.CitasViewModel
import pe.edu.upeu.andinasaludra.presentation.detalle.DetalleCitaScreen
import pe.edu.upeu.andinasaludra.presentation.inicio.InicioScreen
import pe.edu.upeu.andinasaludra.presentation.navigation.PlatformBackHandler
import pe.edu.upeu.andinasaludra.presentation.navigation.Ruta
import pe.edu.upeu.andinasaludra.presentation.perfil.PerfilScreen
import pe.edu.upeu.andinasaludra.presentation.solicitud.SolicitudScreen
import pe.edu.upeu.andinasaludra.presentation.theme.AndinaSaludTheme

// 1. Cambiamos ImageVector por String para usar Emojis
private data class Destino(
    val ruta: Ruta,
    val titulo: String,
    val icono: String
)

// 2. Usamos emojis nativos (no requieren librerías)
private val destinos = listOf(
    Destino(Ruta.Inicio, "Inicio", "🏠"),
    Destino(Ruta.Citas, "Citas", "📅"),
    Destino(Ruta.Perfil, "Perfil", "👤")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() = KoinContext {

    var darkTheme by rememberSaveable { mutableStateOf(false) }
    var ruta by remember { mutableStateOf<Ruta>(Ruta.Inicio) }
    var principalAnterior by remember { mutableStateOf<Ruta>(Ruta.Inicio) }

    val citasViewModel: CitasViewModel = koinViewModel()
    val citasState by citasViewModel.uiState.collectAsState()

    fun navegar(nueva: Ruta) {
        if (nueva is Ruta.Inicio || nueva is Ruta.Citas || nueva is Ruta.Perfil) {
            principalAnterior = nueva
        }
        ruta = nueva
        if (nueva is Ruta.Inicio || nueva is Ruta.Citas) {
            citasViewModel.cargar()
        }
    }

    fun volver() {
        ruta = principalAnterior
        if (principalAnterior is Ruta.Inicio || principalAnterior is Ruta.Citas) {
            citasViewModel.cargar()
        }
    }

    PlatformBackHandler(
        enabled = ruta is Ruta.Detalle || ruta is Ruta.Solicitud
    ) {
        volver()
    }

    AndinaSaludTheme(darkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            when (ruta) {
                                Ruta.Inicio -> "AndinaSalud"
                                Ruta.Citas -> "Mis citas"
                                Ruta.Perfil -> "Perfil"
                                is Ruta.Detalle -> "Detalle de cita"
                                Ruta.Solicitud -> "Solicitar cita"
                            }
                        )
                    },
                    navigationIcon = {
                        if (ruta is Ruta.Detalle || ruta is Ruta.Solicitud) {
                            IconButton(onClick = { volver() }) {
                                Text("<")
                            }
                        }
                    }
                )
            },
            bottomBar = {
                if (ruta is Ruta.Inicio || ruta is Ruta.Citas || ruta is Ruta.Perfil) {
                    NavigationBar {
                        destinos.forEach { destino ->
                            NavigationBarItem(
                                selected = ruta == destino.ruta,
                                onClick = { navegar(destino.ruta) },
                                icon = {
                                    if (destino.ruta == Ruta.Citas) {
                                        BadgedBox(
                                            badge = {
                                                Badge {
                                                    Text(citasState.cantidadProgramadas.toString())
                                                }
                                            }
                                        ) {
                                            // 3. Reemplazamos el Icon(...) por un simple Text(...)
                                            Text(text = destino.icono)
                                        }
                                    } else {
                                        // 3. Reemplazamos el Icon(...) por un simple Text(...)
                                        Text(text = destino.icono)
                                    }
                                },
                                label = { Text(destino.titulo) }
                            )
                        }
                    }
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when (val r = ruta) {
                    Ruta.Inicio -> {
                        InicioScreen(
                            viewModel = koinViewModel(),
                            puedeSolicitar = citasState.puedeSolicitar,
                            onMisCitas = { navegar(Ruta.Citas) },
                            onSolicitar = {
                                if (citasState.puedeSolicitar) navegar(Ruta.Solicitud)
                            },
                            onDetalle = { id -> navegar(Ruta.Detalle(id)) }
                        )
                    }
                    Ruta.Citas -> {
                        CitasScreen(
                            viewModel = citasViewModel,
                            puedeSolicitar = citasState.puedeSolicitar,
                            onDetalle = { id -> navegar(Ruta.Detalle(id)) },
                            onSolicitar = {
                                if (citasState.puedeSolicitar) navegar(Ruta.Solicitud)
                            }
                        )
                    }
                    Ruta.Perfil -> {
                        PerfilScreen(
                            viewModel = koinViewModel(),
                            darkTheme = darkTheme
                        ) { nuevoTema ->
                            darkTheme = nuevoTema
                        }
                    }
                    is Ruta.Detalle -> {
                        DetalleCitaScreen(
                            id = r.citaId,
                            viewModel = koinViewModel()
                        )
                    }
                    Ruta.Solicitud -> {
                        SolicitudScreen(
                            viewModel = koinViewModel()
                        ) { id ->
                            navegar(Ruta.Detalle(id))
                        }
                    }
                }
            }
        }
    }
}
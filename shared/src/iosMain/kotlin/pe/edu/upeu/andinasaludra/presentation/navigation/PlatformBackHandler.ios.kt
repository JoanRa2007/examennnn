package pe.edu.upeu.andinasaludra.presentation.navigation

import androidx.compose.runtime.Composable

// iOS no tiene un botón de retroceso del sistema equivalente al de Android;
// la navegación "atrás" se maneja desde la UI (flecha en el TopAppBar).
@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    // No-op en iOS.
}

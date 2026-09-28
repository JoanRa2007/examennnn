# AndinaSalud – Gestión de citas médicas (KMP)

Aplicación móvil multiplataforma (Android e iOS) desarrollada con **Kotlin Multiplatform** y **Compose Multiplatform** para el examen parcial de la Unidad 1 de Desarrollo de Aplicaciones Móviles (UPeU, 2026-2).

Los datos provienen de una fuente simulada en memoria. No usa red ni base de datos. La arquitectura permite conectar una API REST reemplazando solo la implementación del repositorio.

## Tecnologías

- Kotlin 2.4.20 · Compose Multiplatform 1.6.11 · Material 3
- Arquitectura Clean + MVVM (ViewModel con `StateFlow` y `UiState`)
- Koin 4.0.0 para inyección de dependencias
- Corrutinas para simular el retardo de carga (800 ms)

## Estructura de paquetes

```
shared/src/
├── commonMain/kotlin/pe/edu/upeu/andinasaludra/
│   ├── domain/
│   │   ├── model/        Cita, EstadoCita (sealed class), Medico, Sede, Paciente, FechaHora...
│   │   ├── repository/   CitaRepository (solo la interfaz)
│   │   ├── time/         Reloj (interfaz; RelojSistema es expect/actual)
│   │   └── usecase/      ObtenerCitas, SolicitarCita, CancelarCita, ReprogramarCita, ReglasCita...
│   ├── data/
│   │   ├── local/        CitasSimuladas (datos en memoria)
│   │   └── repository/   CitaRepositoryFake (implementa la interfaz)
│   ├── presentation/
│   │   ├── inicio/ citas/ detalle/ solicitud/ perfil/   (Screen + ViewModel + UiState)
│   │   ├── components/   CitaCard, Estados (carga, vacío, error)
│   │   ├── navigation/   Destinos, PlatformBackHandler (expect/actual)
│   │   └── theme/        Color, Type, AndinaSaludTheme
│   └── di/               AppModule (módulos Koin)
├── androidMain/          actual de RelojSistema, PlatformBackHandler, platformModule
└── iosMain/              actual de RelojSistema, PlatformBackHandler, platformModule
```

## Decisiones de arquitectura

- **Reglas de negocio en el dominio.** Las cinco reglas (RN-01 a RN-05) viven en `ReglasCita.kt` y las aplican los casos de uso. La interfaz solo muestra los errores que recibe.
- **Estado de la cita como `sealed class`.** Cada estado lleva su propia información: `Programada` (recordatorio), `Atendida` (indicaciones) y `Cancelada` (motivo y quién canceló). Un enum o un texto no podrían guardar esos datos.
- **Repositorio como interfaz en `domain`.** `CitaRepositoryFake` es la implementación simulada. Para conectar una API basta con crear otra implementación de `CitaRepository` y cambiar una línea en `dataModule` de `AppModule.kt`. La UI y los casos de uso no cambian.
- **ViewModel con `StateFlow`.** El estado mutable es privado (`_uiState`) y solo se expone `StateFlow` de solo lectura.
- **`expect`/`actual` para lo dependiente de plataforma.** `RelojSistema` (usa `Calendar` en Android y `NSCalendar` en iOS), `PlatformBackHandler` y `platformModule`.
- **Koin.** Los módulos (`dataModule`, `domainModule`, `presentationModule`) están en `commonMain`. Se inicializa con `initKoin()` desde `MainActivity` (Android) y `iOSApp` (iOS).

## Requerimientos funcionales

| Código | Pantalla / función | Ubicación |
|---|---|---|
| RF-01 | Inicio con próxima cita y accesos rápidos | `presentation/inicio` |
| RF-02 | Lista ordenada con filtro por estado | `presentation/citas` |
| RF-03 | Detalle y cancelación con confirmación | `presentation/detalle` |
| RF-04 | Solicitud de cita con validación por campo | `presentation/solicitud` |
| RF-05 | Búsqueda sin distinguir mayúsculas ni tildes | `CitasViewModel` |
| RF-06 | Perfil y tema claro/oscuro | `presentation/perfil` |
| RF-07 | Navegación inferior y botón atrás | `App.kt`, `navigation` |
| RF-08 | Estados de carga, vacío y error | `presentation/components/Estados.kt` |

## Cómo ejecutar

**Android:** abrir el proyecto en Android Studio, esperar la sincronización de Gradle y ejecutar la configuración `androidApp`. También con `./gradlew :androidApp:assembleDebug`.

**iOS:** requiere macOS. Abrir la carpeta `iosApp` en Xcode y ejecutar en un simulador.

## Flujo de trabajo en Git

- `main`: código estable.
- `develop`: integración de los cambios.
- `sc-<letra>-<apellido>`: rama de la solicitud de cambio (Parte II).
- Mensajes de commit con prefijos `feat`, `fix`, `refactor`, `style` y `docs`.

## Reparto de trabajo

Proyecto desarrollado de forma individual, con autorización del docente.

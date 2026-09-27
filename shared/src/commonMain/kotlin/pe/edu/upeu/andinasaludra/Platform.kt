package pe.edu.upeu.andinasaludra

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
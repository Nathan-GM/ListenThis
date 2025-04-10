package dam.nathan

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
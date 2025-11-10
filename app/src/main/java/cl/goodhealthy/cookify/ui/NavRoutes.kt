package cl.goodhealthy.cookify.ui

/**
 * Rutas de navegación centralizadas.
 * Empezamos con Home y Detalle. Luego añadimos más (Favoritos, etc.)
 */
object NavRoutes {

    // Auth
    const val LOGIN = "login"
    const val REGISTER = "register"

    // App
    const val HOME = "home"
    const val FAVORITES = "favorites"
    const val BY_TIME = "byTime"
    const val BY_LETTER = "byLetter"
    const val SETTINGS = "settings"
    const val DETAIL = "detail/{id}"   // {id} se reemplaza por el slug de la receta
}

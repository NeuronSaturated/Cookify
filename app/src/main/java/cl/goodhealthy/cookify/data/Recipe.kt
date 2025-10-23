package cl.goodhealthy.cookify.data


/**
 * Modelo de datos que representa una receta individual.
 *
 * Cada propiedad refleja un campo del archivo JSON que generaste
 * (recipes_gourmet.json) y que se guardó en la carpeta assets.
 *
 * El operador "=" con valor por defecto evita errores si algún
 * campo viene nulo o faltante en el JSON.
 */
data class Recipe(
    val id: String = "",                    // Identificador único (slug o nombre)
    val title: String = "",                 // Título de la receta (ej: "Cachitos de Manjar")
    val description: String = "",           // Breve descripción de la receta
    val imageUrl: String = "",              // URL de la imagen de la receta
    val totalMinutes: Long? = null,         // Tiempo total de preparación (puede faltar)
    val servings: String? = null,           // Cantidad de porciones (opcional)
    val updatedAt: String? = null,          // Fecha de última actualización (texto)
    val categories: List<String> = emptyList(),  // Lista de categorías (postres, chilena, etc.)
    val ingredients: List<String> = emptyList(), // Lista de ingredientes
    val steps: List<String> = emptyList(),       // Pasos de preparación
    val sourceUrl: String = ""               // URL de la receta original en la web
)

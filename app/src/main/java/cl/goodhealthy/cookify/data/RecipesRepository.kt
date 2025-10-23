package cl.goodhealthy.cookify.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader

/**
 * Repositorio que se encarga de cargar y entregar recetas.
 *
 * Lee el archivo recipes_gourmet.json desde la carpeta assets de la app.
 * Usa lectura "streaming" (JsonReader) para manejar archivos grandes sin
 * ocupar mucha memoria.
 */
class RecipesRepository(private val context: Context) {

    // Lista en memoria que guarda todas las recetas cargadas.
    private val cache = mutableListOf<Recipe>()

    // Bandera para no volver a cargar el archivo si ya está en memoria.
    private var loaded = false

    /**
     * Devuelve todas las recetas disponibles.
     */
    fun getAll(): List<Recipe> {
        ensureLoaded()
        return cache
    }

    /**
     * Busca una receta por su id (slug único).
     */
    fun getById(id: String): Recipe? {
        ensureLoaded()
        return cache.firstOrNull { it.id == id }
    }

    /**
     * Busca recetas cuyo tiempo total sea menor o igual al valor indicado.
     */
    fun searchByMaxMinutes(max: Int): List<Recipe> {
        ensureLoaded()
        return cache.filter { it.totalMinutes?.let { m -> m <= max } == true }
    }

    /**
     * Método interno: carga el JSON desde assets si aún no se ha hecho.
     */
    private fun ensureLoaded() {
        if (loaded) return

        // Abre el archivo recipes_gourmet.json dentro de assets/
        context.assets.open("recipes_gourmet.json").use { input ->
            JsonReader(InputStreamReader(input)).use { reader ->
                val gson = Gson()

                // Indica que el JSON es un array grande "[ ... ]"
                reader.beginArray()

                // Lee cada receta una por una y las agrega a la lista
                while (reader.hasNext()) {
                    val item = gson.fromJson<Recipe>(reader, Recipe::class.java)
                    cache.add(item)
                }

                reader.endArray()
            }
        }
        loaded = true

        // Limpieza opcional de categorías "basura" del sitio web
        val blacklist = setOf(
            "inicio", "Recetas", "ver receta", "ver todas las recetas",
            "Empresa Gourmet", "Contacto", "Seguimiento del envío",
            "Preguntas frecuentes", "Política de privacidad",
            "GOURMETCHILE", "CLUBGOURMETTV", "aquí"
        )

        // Reemplaza cada receta con una versión filtrada
        for (i in cache.indices) {
            val r = cache[i]
            cache[i] = r.copy(
                categories = r.categories.filter { it.isNotBlank() && it !in blacklist }
            )
        }
    }
}

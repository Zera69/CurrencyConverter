package com.example.currencyconverter.datos

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PersonajeManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("personajes_db", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val personajesKey = "personajes_list"

    /**
     * Guardar un personaje en la lista de personajes
     */
    fun guardarPersonaje(personaje: Personaje) {
        val personajesActuales = obtenerPersonajes().toMutableList()

        // Si el personaje ya existe, lo reemplazamos
        val indice = personajesActuales.indexOfFirst { it.nombre == personaje.nombre }
        if (indice != -1) {
            personajesActuales[indice] = personaje
        } else {
            personajesActuales.add(personaje)
        }

        // Guardamos la lista actualizada
        val json = gson.toJson(personajesActuales)
        sharedPreferences.edit().putString(personajesKey, json).apply()
    }

    /**
     * Obtener todos los personajes guardados
     */
    fun obtenerPersonajes(): List<Personaje> {
        val json = sharedPreferences.getString(personajesKey, null) ?: return emptyList()

        return try {
            val type = object : TypeToken<List<Personaje>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun obtenerPersonaje(nombre: String): Personaje? {
        return obtenerPersonajes().find { it.nombre == nombre }
    }

    fun eliminarPersonaje(nombre: String) {
        val personajesActuales = obtenerPersonajes().toMutableList()
        personajesActuales.removeAll { it.nombre == nombre }

        val json = gson.toJson(personajesActuales)
        sharedPreferences.edit().putString(personajesKey, json).apply()
    }

    fun obtenerNombresPersonajes(): List<String> {
        return obtenerPersonajes().map { it.nombre }
    }

    fun limpiarPersonajes() {
        sharedPreferences.edit().remove(personajesKey).apply()
    }
}


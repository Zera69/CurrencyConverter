package com.example.currencyconverter.datos

data class Ataque(
    val nombre: String,
    val cantidadDados: Int,
    val carasDado: Int,
    val bonoDano: Int
) {
    // Calcula el daño promedio: ((caras/2)+0.5) * cantidad + bono
    fun calcularDanoPromedio(): Double {
        return ((carasDado / 2.0) + 0.5) * cantidadDados + bonoDano
    }
}

data class Personaje(
    var nombre: String,
    var vida: Int,
    var ac: Int,
    val ataques: MutableList<Ataque> = mutableListOf()
)
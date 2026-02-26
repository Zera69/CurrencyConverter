package com.example.currencyconverter.datos

// Clase moneda, representa un tipo de moneda individual
data class Moneda(
    val nombre: String,        // Ej: "Corona"
    val nacion: String,         // Ej: "Būrach"
    val tipo: String,           //"Alta", "Media", "Baja"
    val valorEnSp: Double,      // 8.0, 0.8, 0.016
    val notas: String           //"Oro impuro con plomo."


)
package com.example.currencyconverter.datos

// Colección de todas las monedas posiblesx
object CatalogoMonedas {
    val todasLasMonedas = listOf(
        // Būrach
        Moneda("Corona", "Būrach", "Alta", 8.0, "Oro impuro con plomo."),
        Moneda("Marco", "Būrach", "Media", 0.8, "Plata baja ley."),
        Moneda("Herrumbre", "Būrach", "Baja", 0.016, "Hierro oxidado."),

        // Ostoya
        Moneda("Leura", "Ostoya", "Alta", 10.0, "Plata negra/Oro."),
        Moneda("Cuervo", "Ostoya", "Media", 1.0, "Cobre pesado volcánico."),
        Moneda("Lasca", "Ostoya", "Baja", 0.05, "Hueso (Sin valor fuera)."),

        // Castilla
        Moneda("Virna", "Castilla", "Alta", 10.0, "Oro brillante."),
        Moneda("Serafín", "Castilla", "Media", 1.0, "Difícil de cambiar."),
        Moneda("Marea", "Castilla", "Baja", 0.05, "Bronce duradero."),

        // Clair
        Moneda("Armile", "Clair", "Alta", 18.0, "Oro fino + Valor artístico."),
        Moneda("Escudo", "Clair", "Media", 1.5, "Plata pesada y estaño."),
        Moneda("Pétalo", "Clair", "Baja", 0.03, "Cobre muy fino."),

        // Morencia
        Moneda("Dima", "Morencia", "Suprema", 100.0, "Platino puro (Lingote)."),
        Moneda("Ducado", "Morencia", "Alta", 10.0, "El estándar internacional."),
        Moneda("Nudo", "Morencia", "Baja", 0.1, "Latón preciso.")
    )
}
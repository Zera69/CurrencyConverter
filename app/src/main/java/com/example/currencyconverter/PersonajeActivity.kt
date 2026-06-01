package com.example.currencyconverter

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.currencyconverter.datos.Ataque
import com.example.currencyconverter.datos.Personaje
import com.example.currencyconverter.datos.PersonajeManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class PersonajeActivity : AppCompatActivity() {

    private lateinit var personajeManager: PersonajeManager

    // Referencias del XML
    private lateinit var editNombrePJ: EditText
    private lateinit var editVida: EditText
    private lateinit var editAC: EditText
    private lateinit var editDados: EditText
    private lateinit var editCaras: EditText
    private lateinit var editBono: EditText
    private lateinit var btnCalcular: Button
    private lateinit var btnGuardarPersonaje: Button
    private lateinit var btnCargarPersonaje: Button
    private lateinit var txtResultado: TextView
    private lateinit var chart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personaje)

        personajeManager = PersonajeManager(this)

        // Referencias del XML
        editNombrePJ = findViewById(R.id.editNombrePJ)
        editVida = findViewById(R.id.editVida)
        editAC = findViewById(R.id.editAC)
        editDados = findViewById(R.id.editDados)
        editCaras = findViewById(R.id.editCaras)
        editBono = findViewById(R.id.editBono)
        btnCalcular = findViewById(R.id.btnCalcular)
        btnGuardarPersonaje = findViewById(R.id.btnGuardarPersonaje)
        btnCargarPersonaje = findViewById(R.id.btnCargarPersonaje)
        txtResultado = findViewById(R.id.txtResultado)
        chart = findViewById(R.id.danoChart)

        btnCalcular.setOnClickListener {
            val cant = editDados.text.toString().toIntOrNull() ?: 0
            val caras = editCaras.text.toString().toIntOrNull() ?: 0
            val bono = editBono.text.toString().toIntOrNull() ?: 0

            val miAtaque = Ataque("Ataque", cant, caras, bono)
            val promedio = miAtaque.calcularDanoPromedio()

            txtResultado.text = "Daño Promedio: $promedio"

            // Llamamos a la función que dibuja el gráfico
            actualizarGrafico(chart, miAtaque)
        }

        // Botón para guardar el personaje
        btnGuardarPersonaje.setOnClickListener {
            guardarPersonaje()
        }

        // Botón para cargar un personaje guardado
        btnCargarPersonaje.setOnClickListener {
            mostrarDialogoCargarPersonaje()
        }
    }

    private fun guardarPersonaje() {
        val nombre = editNombrePJ.text.toString().trim()
        val vida = editVida.text.toString().toIntOrNull() ?: 0
        val ac = editAC.text.toString().toIntOrNull() ?: 0

        if (nombre.isEmpty()) {
            txtResultado.text = "Por favor ingresa un nombre para el personaje"
            return
        }

        if (vida <= 0 || ac <= 0) {
            txtResultado.text = "La vida y AC deben ser mayores a 0"
            return
        }

        val cant = editDados.text.toString().toIntOrNull() ?: 0
        val caras = editCaras.text.toString().toIntOrNull() ?: 0
        val bono = editBono.text.toString().toIntOrNull() ?: 0

        val ataque = if (cant > 0 && caras > 0) {
            Ataque("Ataque", cant, caras, bono)
        } else {
            Ataque("Ataque", 0, 0, 0)
        }

        val personaje = Personaje(nombre, vida, ac, mutableListOf(ataque))
        personajeManager.guardarPersonaje(personaje)

        txtResultado.text = "Personaje '$nombre' guardado exitosamente"
    }

    private fun mostrarDialogoCargarPersonaje() {
        val nombresPersonajes = personajeManager.obtenerNombresPersonajes()

        if (nombresPersonajes.isEmpty()) {
            txtResultado.text = "No hay personajes guardados aún"
            return
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona un Personaje")
        builder.setItems(nombresPersonajes.toTypedArray()) { _, which ->
            val nombreSeleccionado = nombresPersonajes[which]
            cargarPersonaje(nombreSeleccionado)
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun cargarPersonaje(nombre: String) {
        val personaje = personajeManager.obtenerPersonaje(nombre)

        if (personaje != null) {
            editNombrePJ.setText(personaje.nombre)
            editVida.setText(personaje.vida.toString())
            editAC.setText(personaje.ac.toString())

            if (personaje.ataques.isNotEmpty()) {
                val ataque = personaje.ataques[0]
                editDados.setText(ataque.cantidadDados.toString())
                editCaras.setText(ataque.carasDado.toString())
                editBono.setText(ataque.bonoDano.toString())

                // Calcular automáticamente el daño
                val promedio = ataque.calcularDanoPromedio()
                txtResultado.text = "Daño Promedio: $promedio"
                actualizarGrafico(chart, ataque)
            }

            txtResultado.text = "Personaje '${personaje.nombre}' cargado exitosamente"
        } else {
            txtResultado.text = "No se pudo cargar el personaje"
        }
    }

    private fun actualizarGrafico(chart: LineChart, ataque: Ataque) {
        val entradas = mutableListOf<Entry>()

        // Calculamos el daño esperado para ACs desde 10 hasta 25
        for (acEnemigo in 10..25) {
            val bonoAtaque = 5 // Suponemos un +5 para impactar por ahora

            // 21 - (AC - Bono) / 20
            val probabilidad = ((21.0 - (acEnemigo - bonoAtaque)) / 20.0).coerceIn(0.05, 0.95)
            val danoEsperado = ataque.calcularDanoPromedio() * probabilidad

            entradas.add(Entry(acEnemigo.toFloat(), danoEsperado.toFloat()))
        }

        val dataSet = LineDataSet(entradas, "Daño esperado vs AC Enemigo")
        dataSet.color = Color.parseColor("#6200EE")
        dataSet.setCircleColor(Color.RED)
        dataSet.lineWidth = 3f
        dataSet.valueTextSize = 10f
        dataSet.setDrawFilled(true) // Rellena el área debajo de la línea
        dataSet.fillColor = Color.LTGRAY

        chart.data = LineData(dataSet)
        chart.description.text = "Armadura del Enemigo (AC)"
        chart.animateX(500) // Animación suave
        chart.invalidate() // Refrescar
    }
}
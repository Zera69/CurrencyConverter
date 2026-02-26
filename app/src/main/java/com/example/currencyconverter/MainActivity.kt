package com.example.currencyconverter

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyconverter.datos.CatalogoMonedas
import com.example.currencyconverter.datos.Moneda

class MainActivity : AppCompatActivity() {

    private lateinit var editCantidad: EditText      // Donde el usuario escribe el número
    private lateinit var spinnerOrigen: Spinner      // Lista desplegable de moneda origen
    private lateinit var spinnerDestino: Spinner     // Lista desplegable de moneda destino
    private lateinit var btnConvertir: Button         // Botón mágico
    private lateinit var textResultado: TextView      // Donde mostraremos el resultado

    // Usamos "?" porque al principio pueden ser null (el usuario aún no ha seleccionado nada)
    private var monedaOrigen: Moneda? = null
    private var monedaDestino: Moneda? = null


    // Este es el punto de entrada de la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Enlazamos con el XML que creamos

        // "findViewById" busca en el XML el elemento con ese ID y lo asigna a nuestra variable
        editCantidad = findViewById(R.id.editCantidadOrigen)
        spinnerOrigen = findViewById(R.id.spinnerOrigen)
        spinnerDestino = findViewById(R.id.spinnerDestino)
        btnConvertir = findViewById(R.id.btnConvertir)
        textResultado = findViewById(R.id.textResultado)

        configurarSpinners()

        // setOnClickListener es como decir: "Cuando hagan click aquí, haz esto"
        btnConvertir.setOnClickListener {
            realizarConversion()  // Llama a la función que hace la magia
        }
    }

    // Esta función prepara los dos spinners para mostrar nuestras monedas
    private fun configurarSpinners() {

        val nombresMonedas = CatalogoMonedas.todasLasMonedas.map { moneda ->
            "${moneda.nombre} (${moneda.nacion} - ${moneda.tipo})"
        }

        // El adaptador es el puente entre nuestros DATOS (lista de nombres) y el SPINNER
        // android.R.layout.simple_spinner_item es un diseño que ya viene con Android
        val adaptador = ArrayAdapter(
            this,                                   // Contexto (la actividad actual)
            android.R.layout.simple_spinner_item,   // Cómo se ve cada elemento cerrado
            nombresMonedas                           // La lista de datos a mostrar
        )

        // Cómo se ve la lista cuando está desplegada
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // --- 3.3 Asignar el adaptador a los spinners ---
        spinnerOrigen.adapter = adaptador
        spinnerDestino.adapter = adaptador

        // ORIGEN: Cuando el usuario selecciona una moneda de origen
        spinnerOrigen.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,  // posición seleccionada (0, 1, 2...)
                                        id: Long) {
                // Guardamos la moneda completa (no solo el nombre) en nuestra variable
                monedaOrigen = CatalogoMonedas.todasLasMonedas[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No pasa nada si no selecciona nada (pero no debería ocurrir)
            }
        }

        // DESTINO: Cuando el usuario selecciona una moneda de destino
        spinnerDestino.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long) {
                monedaDestino = CatalogoMonedas.todasLasMonedas[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No pasa nada
            }
        }

        // Para que no estén vacíos al iniciar la app
        spinnerOrigen.setSelection(0)   // Selecciona la primera moneda (Corona)
        spinnerDestino.setSelection(0)  // También la primera

        // Inicializamos nuestras variables con esas selecciones por defecto
        monedaOrigen = CatalogoMonedas.todasLasMonedas[0]
        monedaDestino = CatalogoMonedas.todasLasMonedas[0]
    }


    // Esta función se ejecuta cuando el usuario pulsa el botón
    private fun realizarConversion() {

        // --- 4.1 Obtener la cantidad que el usuario escribió ---
        val cantidadTexto = editCantidad.text.toString()

        // ¿El usuario no escribió nada?
        if (cantidadTexto.isEmpty()) {
            textResultado.text = "Escribe una cantidad"
            return  // Salimos de la función aquí
        }


        try {
            val cantidad = cantidadTexto.toDouble()  // Puede lanzar excepción si no es número

            // --- 4.3 Verificar que hay monedas seleccionadas ---
            if (monedaOrigen == null || monedaDestino == null) {
                textResultado.text = " Error interno: monedas no seleccionadas"
                return
            }

            // Usamos "!!" porque ya comprobamos que no son null
            // Convertimos la cantidad de origen a sp (plata universal)
            val cantidadEnSp = cantidad * monedaOrigen!!.valorEnSp

            // Convertimos de sp a la moneda destino
            val cantidadDestino = cantidadEnSp / monedaDestino!!.valorEnSp

            // String.format permite crear textos con números formateados
            // "%.2f" significa "número con 2 decimales"
            textResultado.text = String.format(
                "%.2f %s = %.2f %s",
                cantidad,                     // Ej: 5.00
                monedaOrigen!!.nombre,         // Ej: Corona
                cantidadDestino,               // Ej: 40.00
                monedaDestino!!.nombre         // Ej: Serafín
            )

        } catch (e: NumberFormatException) {
            // Si el usuario escribió algo que no es un número (ej: "hola")
            textResultado.text = "Eso no es un número válido"
        }
    }
}
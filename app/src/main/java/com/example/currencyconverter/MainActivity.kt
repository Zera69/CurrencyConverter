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

    private lateinit var editCantidad: EditText
    private lateinit var spinnerOrigen: Spinner
    private lateinit var spinnerDestino: Spinner
    private lateinit var btnConvertir: Button
    private lateinit var textResultado: TextView

    // usamos "?" porque al principio pueden ser null (el usuario aún no ha seleccionado nada)
    private var monedaOrigen: Moneda? = null
    private var monedaDestino: Moneda? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // le indicamos que este es el xml

        // "findViewById" busca en el XML con ese ID y lo asigna
        editCantidad = findViewById(R.id.editCantidadOrigen)
        spinnerOrigen = findViewById(R.id.spinnerOrigen)
        spinnerDestino = findViewById(R.id.spinnerDestino)
        btnConvertir = findViewById(R.id.btnConvertir)
        textResultado = findViewById(R.id.textResultado)

        configurarSpinners()

        // fam, está esperando a que hagas click para hacer algo
        btnConvertir.setOnClickListener {
            realizarConversion()
        }
    }

    private fun configurarSpinners() {

        val nombresMonedas = CatalogoMonedas.todasLasMonedas.map { moneda ->
            "${moneda.nombre} (${moneda.nacion} - ${moneda.tipo})"
        }

        val adaptador = ArrayAdapter(
            this,                                   // Contexto (la actividad actual)
            android.R.layout.simple_spinner_item,   // Como se ve cada elemento cerrado
            nombresMonedas                           // La lista de datos a mostrar
        )

        // Como se ve la lista cuando está desplegada
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerOrigen.adapter = adaptador
        spinnerDestino.adapter = adaptador

        spinnerOrigen.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,  // posición seleccionada (0, 1, 2...)
                                        id: Long) {
                // Guardamos la moneda completa (no solo el nombre) en nuestra variable
                monedaOrigen = CatalogoMonedas.todasLasMonedas[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No pasa nada si no selecciona nada
            }
        }

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
        spinnerOrigen.setSelection(0)
        spinnerDestino.setSelection(0)

        // Inicializamos nuestras variables con esas selecciones por defecto
        monedaOrigen = CatalogoMonedas.todasLasMonedas[0]
        monedaDestino = CatalogoMonedas.todasLasMonedas[0]
    }
    private fun realizarConversion() {

        val cantidadTexto = editCantidad.text.toString()

        if (cantidadTexto.isEmpty()) {
            textResultado.text = "Escribe una cantidad"
            return
        }


        try {
            val cantidad = cantidadTexto.toDouble()  // Puede lanzar excepción si no es número

            if (monedaOrigen == null || monedaDestino == null) {
                textResultado.text = " Error: monedas no seleccionadas"
                return
            }

            // Usamos "!!" porque no puede ser null o caca
            // Convertimos la cantidad de origen a sp
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
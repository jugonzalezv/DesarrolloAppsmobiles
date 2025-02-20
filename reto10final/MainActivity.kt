package com.example.reto10

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CovidAdapter
    private lateinit var btnCargar: Button
    private lateinit var etCiudad: EditText  // EditText para ingresar la ciudad
    private val covidList = mutableListOf<CovidCase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencias a los elementos del layout
        recyclerView = findViewById(R.id.rvPuntos)
        btnCargar = findViewById(R.id.btnCargar)
        etCiudad = findViewById(R.id.etCiudad) // Referencia al EditText de la ciudad

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CovidAdapter(covidList)
        recyclerView.adapter = adapter

        // Evento del botón para cargar datos
        btnCargar.setOnClickListener {
            val ciudad = etCiudad.text.toString().trim()
            if (ciudad.isNotEmpty()) {
                fetchCovidData(ciudad)  // Llamar la función con el nombre de la ciudad
            } else {
                Toast.makeText(this, "Por favor ingrese una ciudad", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchCovidData(ciudad: String) {
        // Codificar la ciudad para usarla en la URL
        val ciudadCodificada = URLEncoder.encode(ciudad, "UTF-8")

        val url = "https://www.datos.gov.co/resource/gt2j-8ykr.json?ciudad_municipio_nom=$ciudadCodificada"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("API", "Error en la conexión", e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val json = responseBody.string()
                    val jsonArray = JSONArray(json)

                    covidList.clear()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val id = item.optString("id_de_caso", "N/A")
                        val fecha = item.optString("fecha_diagnostico", "Sin fecha")
                        val ciudad = item.optString("ciudad_municipio_nom", "Desconocido")
                        val estado = item.optString("estado", "Sin estado")

                        val covidCase = CovidCase(id, fecha, ciudad, estado)
                        covidList.add(covidCase)
                    }

                    runOnUiThread {
                        adapter.notifyDataSetChanged()  // Notificar al adapter que los datos han cambiado
                    }
                }
            }
        })
    }
}

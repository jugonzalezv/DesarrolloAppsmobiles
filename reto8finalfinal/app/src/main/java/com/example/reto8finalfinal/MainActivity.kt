package com.example.reto8finalfinal

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: SQLiteHelper
    private lateinit var empresaAdapter: EmpresaAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var empresas: MutableList<Empresa>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = SQLiteHelper(this)

        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtUrl = findViewById<EditText>(R.id.txtUrl)
        val txtTelefono = findViewById<EditText>(R.id.txtTelefono)
        val txtEmail = findViewById<EditText>(R.id.txtEmail)
        val txtProductos = findViewById<EditText>(R.id.txtProductos)
        val txtClasificacion = findViewById<EditText>(R.id.txtClasificacion)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        cargarEmpresas()

        btnAgregar.setOnClickListener {
            val nombre = txtNombre.text.toString()
            val url = txtUrl.text.toString()
            val telefono = txtTelefono.text.toString()
            val email = txtEmail.text.toString()
            val productos = txtProductos.text.toString()
            val clasificacion = txtClasificacion.text.toString()

            if (nombre.isNotEmpty() && url.isNotEmpty() && telefono.isNotEmpty() && email.isNotEmpty() && productos.isNotEmpty() && clasificacion.isNotEmpty()) {
                dbHelper.agregarEmpresa(nombre, url, telefono, email, productos, clasificacion)
                cargarEmpresas()
                Toast.makeText(this, "Empresa agregada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarEmpresas() {
        empresas = dbHelper.obtenerEmpresas().map { empresa ->
            Empresa(
                id = empresa["id"]!!.toInt(),
                nombre = empresa["nombre"]!!,
                url = empresa["url"]!!,
                telefono = empresa["telefono"]!!,
                email = empresa["email"]!!,
                productos = empresa["productos"]!!,
                clasificacion = empresa["clasificacion"]!!
            )
        }.toMutableList()

        empresaAdapter = EmpresaAdapter(empresas) { empresa -> confirmarEliminacion(empresa) }
        recyclerView.adapter = empresaAdapter
    }

    private fun confirmarEliminacion(empresa: Empresa) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Empresa")
            .setMessage("¿Seguro que deseas eliminar ${empresa.nombre}?")
            .setPositiveButton("Sí") { _, _ ->
                dbHelper.eliminarEmpresa(empresa.id)
                cargarEmpresas()
            }
            .setNegativeButton("No", null)
            .show()
    }
}

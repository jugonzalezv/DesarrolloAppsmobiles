package com.example.reto8finalfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmpresaAdapter(
    private val empresas: List<Empresa>,
    private val onEliminarClick: (Empresa) -> Unit
) : RecyclerView.Adapter<EmpresaAdapter.EmpresaViewHolder>() {

    class EmpresaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.txtNombre)
        val url: TextView = view.findViewById(R.id.txtUrl)
        val telefono: TextView = view.findViewById(R.id.txtTelefono)
        val email: TextView = view.findViewById(R.id.txtEmail)
        val productos: TextView = view.findViewById(R.id.txtProductos)
        val clasificacion: TextView = view.findViewById(R.id.txtClasificacion)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpresaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_empresa, parent, false)
        return EmpresaViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmpresaViewHolder, position: Int) {
        val empresa = empresas[position]
        holder.nombre.text = empresa.nombre
        holder.url.text = empresa.url
        holder.telefono.text = empresa.telefono
        holder.email.text = empresa.email
        holder.productos.text = empresa.productos
        holder.clasificacion.text = empresa.clasificacion

        holder.btnEliminar.setOnClickListener { onEliminarClick(empresa) }
    }

    override fun getItemCount(): Int = empresas.size
}

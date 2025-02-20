package com.example.reto10

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PuntosAdapter(private val listaPuntos: List<PuntoPosconsumo>) :
    RecyclerView.Adapter<PuntosAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.tvNombre)
        val direccion: TextView = view.findViewById(R.id.tvDireccion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_punto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val punto = listaPuntos[position]
        holder.nombre.text = punto.nombre_punto
        holder.direccion.text = punto.direccion
    }

    override fun getItemCount() = listaPuntos.size
}

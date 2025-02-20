package com.example.reto10

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CovidAdapter(private val covidList: List<CovidCase>) :
    RecyclerView.Adapter<CovidAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textId: TextView = view.findViewById(R.id.textId)
        val textFecha: TextView = view.findViewById(R.id.textFecha)
        val textCiudad: TextView = view.findViewById(R.id.textCiudad)
        val textEstado: TextView = view.findViewById(R.id.textEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_covid_case, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val case = covidList[position]
        holder.textId.text = "ID: ${case.id}"
        holder.textFecha.text = "Fecha: ${case.fecha}"
        holder.textCiudad.text = "Ciudad: ${case.ciudad}"
        holder.textEstado.text = "Estado: ${case.estado}"
    }

    override fun getItemCount() = covidList.size
}

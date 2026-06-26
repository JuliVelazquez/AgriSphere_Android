package com.example.agrisphere

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InvernaderoAdapter(private val lista: List<Invernadero>) : RecyclerView.Adapter<InvernaderoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombreInv: TextView = view.findViewById(R.id.tvNombreInv)
        val tvCultivo: TextView = view.findViewById(R.id.tvCultivo)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val tvRevision: TextView = view.findViewById(R.id.tvRevision)
        val tvResponsable: TextView = view.findViewById(R.id.tvResponsable)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_invernadero, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.tvNombreInv.text = item.nombre
        holder.tvCultivo.text = "(${item.cultivo})"
        holder.tvEstado.text = item.estado
        holder.tvRevision.text = item.ultimaRevision
        holder.tvResponsable.text = item.responsable
    }

    override fun getItemCount() = lista.size
}
package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.PontoTuristico
import com.example.myapplication.PontoTuristicoDetalhe

class PontoTuristicoAdapter(
    private val pontosTuristicos: List<PontoTuristico>,
    private val context: Context
) : RecyclerView.Adapter<PontoTuristicoAdapter.PontoTuristicoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PontoTuristicoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ponto_turistico, parent, false)
        return PontoTuristicoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PontoTuristicoViewHolder, position: Int) {
        val ponto = pontosTuristicos[position]
        holder.nome.text = ponto.nome
        holder.info1.text = ponto.info1
        holder.info2.text = ponto.info2
        holder.info3.text = ponto.info3

        // Quando o item for clicado, navega para a PontoTuristicoDetalheActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(context, PontoTuristicoDetalhe::class.java)
            intent.putExtra("PONTO_TURISTICO", ponto)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return pontosTuristicos.size
    }

    class PontoTuristicoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.textViewNomePonto)
        val info1: TextView = itemView.findViewById(R.id.infoPontoTuristico1)
        val info2: TextView = itemView.findViewById(R.id.infoPontoTuristico2)
        val info3: TextView = itemView.findViewById(R.id.infoPontoTuristico3)
    }
}

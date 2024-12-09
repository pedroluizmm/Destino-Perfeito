package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FavoritesAdapter(
    private val items: List<FavoriteItem>
) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ponto_turistico, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewNomePonto)
        private val info1TextView: TextView = itemView.findViewById(R.id.infoPontoTuristico1)
        private val info2TextView: TextView = itemView.findViewById(R.id.infoPontoTuristico2)
        private val info3TextView: TextView = itemView.findViewById(R.id.infoPontoTuristico3)

        fun bind(item: FavoriteItem) {
            nameTextView.text = item.name
            info1TextView.text = "Descrição: ${item.description}" // Pode ser adaptado conforme necessário
            info2TextView.visibility = View.GONE // Caso não precise de mais informações
            info3TextView.visibility = View.GONE
        }
    }
}

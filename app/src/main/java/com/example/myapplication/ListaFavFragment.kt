package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class ListaFavFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var favoritosAdapter: PontoTuristicoAdapter
    private lateinit var listaFavoritos: MutableList<PontoTuristico>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar o layout para o Fragment
        return inflater.inflate(R.layout.fragment_lista_fav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa a RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewFavoritos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Recupera os favoritos salvos em SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("FAVORITOS", Context.MODE_PRIVATE)
        val favoritosJson = sharedPreferences.all.values // Obtém todos os favoritos salvos
        listaFavoritos = mutableListOf()

        // Converte o JSON salvo para objetos do tipo PontoTuristico
        val gson = Gson()
        favoritosJson.forEach { favorito ->
            val pontoTuristico = gson.fromJson(favorito.toString(), PontoTuristico::class.java)
            listaFavoritos.add(pontoTuristico)
        }

        // Configura o adapter
        favoritosAdapter = PontoTuristicoAdapter(listaFavoritos, requireContext())
        recyclerView.adapter = favoritosAdapter
    }

    override fun onResume() {
        super.onResume()
        atualizarListaFavoritos()
    }

    private fun atualizarListaFavoritos() {
        val sharedPreferences = requireContext().getSharedPreferences("FAVORITOS", Context.MODE_PRIVATE)
        val favoritosJson = sharedPreferences.all.values
        val gson = Gson()

        val novaListaFavoritos = favoritosJson.map { favorito ->
            gson.fromJson(favorito.toString(), PontoTuristico::class.java)
        }

        val itensRemovidos = listaFavoritos.filterNot { it in novaListaFavoritos }
        val itensAdicionados = novaListaFavoritos.filterNot { it in listaFavoritos }

        // Remove itens antigos
        itensRemovidos.forEach { item ->
            val index = listaFavoritos.indexOf(item)
            if (index != -1) {
                listaFavoritos.removeAt(index)
                favoritosAdapter.notifyItemRemoved(index)
            }
        }

        // Adiciona novos itens
        itensAdicionados.forEach { item ->
            listaFavoritos.add(item)
            val index = listaFavoritos.indexOf(item)
            favoritosAdapter.notifyItemInserted(index)
        }
    }
}

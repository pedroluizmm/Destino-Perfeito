package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import android.widget.Toast

class ListaFavFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var favoritosAdapter: PontoTuristicoAdapter
    private lateinit var listaFavoritos: MutableList<PontoTuristico>

    private val favoritesManager = FavoritesManager()
    private lateinit var favoritesAdapter: FavoritesAdapter
    private val favoriteItems = mutableListOf<FavoriteItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lista_fav, container, false)

        // Configurar RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewFavoritos)
        recyclerView.layoutManager = LinearLayoutManager(context)
        favoritesAdapter = FavoritesAdapter(favoriteItems)
        recyclerView.adapter = favoritesAdapter

        loadFavorites()

        return view
    }

    private fun loadFavorites() {
        favoritesManager.getFavoriteItems({ items ->
            favoriteItems.clear()
            favoriteItems.addAll(items)
            favoritesAdapter.notifyDataSetChanged()
        }, { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewFavoritos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val sharedPreferences = requireContext().getSharedPreferences("FAVORITOS", Context.MODE_PRIVATE)
        val favoritosJson = sharedPreferences.all.values
        listaFavoritos = mutableListOf()

        val gson = Gson()
        favoritosJson.forEach { favorito ->
            val pontoTuristico = gson.fromJson(favorito.toString(), PontoTuristico::class.java)
            listaFavoritos.add(pontoTuristico)
        }

        favoritosAdapter = PontoTuristicoAdapter(listaFavoritos, requireContext())
        recyclerView.adapter = favoritosAdapter
    }

    override fun onResume() {
        super.onResume()
        atualizarListaFavoritos()
    }

    private fun atualizarListaFavoritos() {
        val sharedPreferences = requireContext().getSharedPreferences("FAVORITOS", MODE_PRIVATE)
        val favoritosJson = sharedPreferences.all.values
        val gson = Gson()

        val novaListaFavoritos = favoritosJson.map { favorito ->
            gson.fromJson(favorito.toString(), PontoTuristico::class.java)
        }

        listaFavoritos.clear()
        listaFavoritos.addAll(novaListaFavoritos)

        favoritosAdapter.notifyDataSetChanged()
    }
}

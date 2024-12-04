package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class ListaFav : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var favoritosAdapter: PontoTuristicoAdapter
    private lateinit var listaFavoritos: MutableList<PontoTuristico>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_fav)

        // Inicializa a RecyclerView
        recyclerView = findViewById(R.id.recyclerViewFavoritos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Recupera os favoritos salvos em SharedPreferences
        val sharedPreferences = getSharedPreferences("FAVORITOS", MODE_PRIVATE)
        val favoritosJson = sharedPreferences.all.values // Obtém todos os favoritos salvos
        listaFavoritos = mutableListOf()

        // Converte o JSON salvo para objetos do tipo PontoTuristico
        val gson = Gson()
        favoritosJson.forEach { favorito ->
            val pontoTuristico = gson.fromJson(favorito.toString(), PontoTuristico::class.java)
            listaFavoritos.add(pontoTuristico)
        }

        // Configura o adapter
        favoritosAdapter = PontoTuristicoAdapter(listaFavoritos, this)
        recyclerView.adapter = favoritosAdapter

        // Configura o BottomNavigationView
        bottomNavigation = findViewById(R.id.bottomNavigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_settings -> {
                    val intent = Intent(this, Perfil::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_catalog -> {
                    val intent = Intent(this, MenuPrincipal::class.java)
                    startActivity(intent)
                    true
                }
                R.id.Mapa -> {
                    val intent = Intent(this, Perfil::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
    override fun onResume() {
        super.onResume()
        atualizarListaFavoritos()
    }

    private fun atualizarListaFavoritos() {
        val sharedPreferences = getSharedPreferences("FAVORITOS", MODE_PRIVATE)
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

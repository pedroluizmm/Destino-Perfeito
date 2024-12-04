package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.TextView
import android.content.Intent

class MenuPrincipal : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var textViewMapa: TextView
    private lateinit var adapter: PontoTuristicoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        bottomNavigation = findViewById(R.id.bottomNavigation)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Usando o repositório para acessar a lista de pontos turísticos
        val listaPontosTuristicos = PontoTuristicoRepositorio.listaPontosTuristicos
        adapter = PontoTuristicoAdapter(listaPontosTuristicos, this)
        recyclerView.adapter = adapter

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_settings -> {
                    val intent = Intent(this, Perfil::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_catalog -> {
                    val intent = Intent(this, ListaFav::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this, Perfil::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        textViewMapa = findViewById(R.id.textViewMapa)
        textViewMapa.setOnClickListener {
            // Ao clicar, navega para uma nova Activity (MapaActivity)
            val intent = Intent(this, Mapa::class.java)
            startActivity(intent)
        }
    }
}
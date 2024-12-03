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
    private lateinit var listaPontosTuristicos: MutableList<PontoTuristico>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        bottomNavigation = findViewById(R.id.bottomNavigation)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        listaPontosTuristicos = mutableListOf(
            PontoTuristico("Beach Park", "Informação 1", "Informação 2", "Informação 3",-3.8459892,-38.3920148),
            PontoTuristico("Parque Estadual do coco", "Informação 1", "Informação 2", "Informação 3",-3.7501652,-38.4865193),
            PontoTuristico("Coco", "Informação 1", "Informação 2", "Informação 3", -3.7179,-38.5267)
            // Adicione mais pontos turísticos aqui
        )

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
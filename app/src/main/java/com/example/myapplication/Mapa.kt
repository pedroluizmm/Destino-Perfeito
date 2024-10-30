package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class Mapa : AppCompatActivity() {

    private val places = arrayListOf(
        Local("Beach Park", LatLng(-3.8459892,-38.3920148),"Rua Porto das Dunas, 2734 - Porto das Dunas, Aquiraz - CE, 61700-000",4.6f ),
        Local("Parque Estadual do coco", LatLng(-3.7501652,-38.4865193),"Av. Padre Antônio Tomás, s/n - Cocó, Fortaleza - CE, 60060-170",4.2f ),

        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mapa)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMaps->
            addMarkers(googleMaps)
        }

    }

    private fun addMarkers(googleMap: GoogleMap) {
        places.forEach() { place ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(place.nome)
                    .snippet(place.endereco)
                    .position(place.latLng)
            )
        }
    }
}

data class Local(
    val nome: String,
    val latLng: LatLng,
    val endereco: String,
    val avalicao: Float,
)

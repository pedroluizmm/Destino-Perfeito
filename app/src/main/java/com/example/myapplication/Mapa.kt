package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener

class Mapa : AppCompatActivity() {

    private var marcadorAnterior: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMaps ->
            addMarkers(googleMaps)

            googleMaps.setOnMarkerClickListener { marker ->
                if (marker.title == marcadorAnterior) {
                    abrirTelaDetalhes(marker.title ?: "")
                    return@setOnMarkerClickListener true
                } else {
                    marcadorAnterior = marker.title
                    return@setOnMarkerClickListener false
                }
            }
        }
    }

    private fun addMarkers(googleMap: GoogleMap) {
        PontoTuristicoRepositorio.listaPontosTuristicos.forEach { place ->
            googleMap.addMarker(
                MarkerOptions()
                    .title(place.nome)
                    .snippet(place.info1)
                    .position(LatLng(place.latitude, place.longitude))
            )
        }
    }

    private fun abrirTelaDetalhes(marker: String) {
        val pontoTuristico = PontoTuristicoRepositorio.listaPontosTuristicos.find { it.nome == marker }

        pontoTuristico?.let {
            val intent = Intent(this, PontoTuristicoDetalhe::class.java)
            intent.putExtra("PONTO_TURISTICO", it)
            startActivity(intent)
        }
    }
}

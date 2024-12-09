package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapaFragment : Fragment() {

    private val localInicial = LatLng(-3.71722, -38.54342) // Fortaleza, CE (exemplo)
    private val zoomInicial = 12f // Zoom inicial para a localização

    private var marcadorAnterior: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla o layout do fragmento
        return inflater.inflate(R.layout.fragment_mapa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMaps ->
            configurarMapa(googleMaps)
            addMarkers(googleMaps)

            googleMaps.setOnMarkerClickListener { marker ->
                if (marker.title == marcadorAnterior) {
                    abrirTelaDetalhes(marker.title ?: "")
                    true
                } else {
                    marcadorAnterior = marker.title
                    false
                }
            }
        }
    }

    private fun configurarMapa(googleMap: GoogleMap) {
        // Define a localização inicial do mapa
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localInicial, zoomInicial))
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
            val intent = Intent(requireContext(), PontoTuristicoDetalhe::class.java)
            intent.putExtra("PONTO_TURISTICO", it)
            startActivity(intent)
        }
    }
}

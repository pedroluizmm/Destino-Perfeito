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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapaFragment : Fragment() {

    private lateinit var googleMap: GoogleMap
    private val localFortaleza = LatLng(-3.71722, -38.54342) // Fortaleza
    private val zoomInicial = 12f
    private var marcadorAnterior: Marker? = null

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
        mapFragment.getMapAsync { map ->
            googleMap = map
            configurarMapa()
            carregarPontosTuristicos()
        }
    }

    private fun configurarMapa() {
        // Centraliza o mapa em Fortaleza
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localFortaleza, zoomInicial))

        // Configura o clique nos marcadores
        googleMap.setOnMarkerClickListener { marker ->
            if (marcadorAnterior != null && marcadorAnterior == marker) {
                abrirDetalhesDoPonto(marker.title)
                true
            } else {
                marcadorAnterior = marker
                false
            }
        }
    }

    private fun carregarPontosTuristicos() {
        // Busca os pontos turísticos do serviço
        PontoTuristicoService.buscarPontosTuristicos(requireContext()) {
            adicionarMarcadores()
        }
    }

    private fun adicionarMarcadores() {
        googleMap.clear() // Limpa marcadores antigos
        PontoTuristicoRepositorio.listaPontosTuristicos.forEach { ponto ->
            googleMap.addMarker(
                MarkerOptions()
                    .position(LatLng(ponto.latitude, ponto.longitude))
                    .title(ponto.nome)
                    .snippet(ponto.info1)
            )
        }
    }

    private fun abrirDetalhesDoPonto(nome: String?) {
        // Busca o ponto turístico pelo nome e abre a tela de detalhes
        val pontoTuristico = PontoTuristicoRepositorio.listaPontosTuristicos.find { it.nome == nome }
        pontoTuristico?.let {
            val intent = Intent(requireContext(), PontoTuristicoDetalhe::class.java)
            intent.putExtra("PONTO_TURISTICO", it)
            startActivity(intent)
        }
    }
}

package com.example.myapplication

import android.content.Context
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest

object PontoTuristicoService {

    private const val API_KEY = "AIzaSyDB5wiLwLtJI31oQ4znjAVqTd9rgM2j4zs"

    fun buscarPontosTuristicos(
        context: Context,
        radius: Int = 10000, // Raio da busca em metros
        callback: () -> Unit
    ) {
        val latitude = -3.71722 // Fortaleza
        val longitude = -38.5434

        if (!Places.isInitialized()) {
            Places.initialize(context, API_KEY)
        }
        val placesClient: PlacesClient = Places.createClient(context)

        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.PHOTO_METADATAS)
        val center = LatLng(latitude, longitude)
        val circularBounds = CircularBounds.newInstance(center, radius.toDouble())
        val includedTypes = listOf("tourist_attraction", "restaurant", "cafe")

        val searchNearbyRequest = SearchNearbyRequest.builder(circularBounds, placeFields)
            .setIncludedTypes(includedTypes)
            .setMaxResultCount(20)
            .build()

        placesClient.searchNearby(searchNearbyRequest)
            .addOnSuccessListener { response ->
                val pontosTuristicos = mutableListOf<PontoTuristico>()

                response.places.forEach { place ->
                    val photoMetadata = place.photoMetadatas?.firstOrNull() // Obtém o primeiro metadado da foto

                    if (photoMetadata != null) {
                        // Configura a solicitação da foto
                        val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                            .setMaxWidth(500)  // Define a largura máxima da imagem
                            .setMaxHeight(500) // Define a altura máxima da imagem
                            .build()

                        placesClient.fetchPhoto(photoRequest)
                            .addOnSuccessListener { fetchPhotoResponse ->
                                val bitmap = fetchPhotoResponse.bitmap // Aqui você pode acessar a foto

                                val fotoPath = bitmap?.let { ImageUtils.salvarBitmapNoCache(context, it) }

                                place.latLng?.let { latLng ->
                                    pontosTuristicos.add(
                                        PontoTuristico(
                                            nome = place.name ?: "Sem nome",
                                            info1 = place.address ?: "Endereço não disponível",
                                            info2 = "",
                                            info3 = "",
                                            latitude = latLng.latitude,
                                            longitude = latLng.longitude,
                                            fotoPath = fotoPath // Adiciona a foto ao objeto
                                        )
                                    )
                                }

                                // Atualiza o repositório e executa o callback
                                PontoTuristicoRepositorio.listaPontosTuristicos.apply {
                                    clear()
                                    addAll(pontosTuristicos)
                                }
                                callback()
                            }
                            .addOnFailureListener { exception ->
                                Log.e("PontoTuristicoService", "Erro ao buscar foto: ${exception.message}")
                            }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("PontoTuristicoService", "Erro ao buscar pontos turísticos: ${exception.message}")
                callback()
            }
    }
}
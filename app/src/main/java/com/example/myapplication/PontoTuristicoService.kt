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
        types: List<String> = emptyList(),
        callback: () -> Unit
    ) {
        val latitude = -3.71722 // Fortaleza
        val longitude = -38.5434

        if (!Places.isInitialized()) {
            Places.initialize(context, API_KEY)
        }
        val placesClient: PlacesClient = Places.createClient(context)

        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.PHOTO_METADATAS,Place.Field.RATING, Place.Field.OPENING_HOURS )
        val center = LatLng(latitude, longitude)
        val circularBounds = CircularBounds.newInstance(center, radius.toDouble())


        val searchNearbyRequest = SearchNearbyRequest.builder(circularBounds, placeFields)
            .setIncludedTypes(types)
            .setMaxResultCount(20)
            .build()

        placesClient.searchNearby(searchNearbyRequest)
            .addOnSuccessListener { response ->
                val pontosTuristicos = mutableListOf<PontoTuristico>()

                response.places.forEach { place ->
                    val photoMetadata = place.photoMetadatas?.firstOrNull()

                    if (photoMetadata != null) {
                        // Configura a solicitação da foto
                        val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                            .setMaxWidth(500)
                            .setMaxHeight(500)
                            .build()

                        placesClient.fetchPhoto(photoRequest)
                            .addOnSuccessListener { fetchPhotoResponse ->
                                val bitmap = fetchPhotoResponse.bitmap

                                val fotoPath = bitmap?.let { ImageUtils.salvarBitmapNoCache(context, it) }

                                place.latLng?.let { latLng ->
                                    pontosTuristicos.add(
                                        PontoTuristico(
                                            nome = place.name ?: "Sem nome",
                                            info1 = place.address ?: "Endereço não disponível",
                                            info2 = place.rating?.toString() ?: "Sem avaliações",
                                            info3 = place.openingHours?.weekdayText?.joinToString("\n") ?: "Horário não disponível", // Horários
                                            latitude = latLng.latitude,
                                            longitude = latLng.longitude,
                                            fotoPath = fotoPath
                                        )
                                    )
                                }

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
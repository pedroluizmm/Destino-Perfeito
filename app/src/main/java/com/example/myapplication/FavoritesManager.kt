package com.example.myapplication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesManager {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Adicionar um item aos favoritos do usuário atual
    fun addFavoriteItem(
        item: FavoriteItem,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userDocRef = db.collection("users").document(userId)

            userDocRef.get().addOnSuccessListener { document ->
                val currentFavorites = document["favorites"] as? ArrayList<Map<String, Any>> ?: arrayListOf()
                val newItem = mapOf(
                    "id" to item.id,
                    "name" to item.name,
                    "description" to item.description
                )

                currentFavorites.add(newItem)
                userDocRef.set(mapOf("favorites" to currentFavorites))
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e.message ?: "Erro ao adicionar favorito") }
            }.addOnFailureListener { e ->
                onFailure(e.message ?: "Erro ao acessar favoritos")
            }
        } else {
            onFailure("Usuário não autenticado")
        }
    }

    // Recuperar os itens favoritos do usuário atual
    fun getFavoriteItems(
        onSuccess: (List<FavoriteItem>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userDocRef = db.collection("users").document(userId)

            userDocRef.get().addOnSuccessListener { document ->
                val currentFavorites = document["favorites"] as? ArrayList<Map<String, Any>> ?: arrayListOf()
                val items = currentFavorites.map {
                    FavoriteItem(
                        id = it["id"].toString(),
                        name = it["name"].toString(),
                        description = it["description"].toString()
                    )
                }
                onSuccess(items)
            }.addOnFailureListener { e ->
                onFailure(e.message ?: "Erro ao recuperar favoritos")
            }
        } else {
            onFailure("Usuário não autenticado")
        }
    }

    // Remover um item dos favoritos do usuário atual
    fun removeFavoriteItem(
        itemId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userDocRef = db.collection("users").document(userId)

            userDocRef.get().addOnSuccessListener { document ->
                val currentFavorites = document["favorites"] as? ArrayList<Map<String, Any>> ?: arrayListOf()
                val updatedFavorites = currentFavorites.filterNot { it["id"] == itemId }

                userDocRef.set(mapOf("favorites" to updatedFavorites))
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e.message ?: "Erro ao remover favorito") }
            }.addOnFailureListener { e ->
                onFailure(e.message ?: "Erro ao acessar favoritos")
            }
        } else {
            onFailure("Usuário não autenticado")
        }
    }
}

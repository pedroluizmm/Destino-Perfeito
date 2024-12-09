package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import android.graphics.BitmapFactory

class PontoTuristicoDetalhe : AppCompatActivity() {
    private val favoritesManager = FavoritesManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ponto_turistico_detalhe)

        // Declara apenas uma vez
        val buttonFavoritar: Button = findViewById(R.id.buttonFavoritar)

        // Recupera os dados do Intent
        val nome = intent.getStringExtra("PONTO_NOME")
        val endereco = intent.getStringExtra("PONTO_ENDERECO")
        val latitude = intent.getDoubleExtra("PONTO_LATITUDE", 0.0)
        val longitude = intent.getDoubleExtra("PONTO_LONGITUDE", 0.0)
        val imagePath = intent.getStringExtra("IMAGE_PATH")
        val avaliacao = intent.getStringExtra("PONTO_AVALIACAO")
        val horario = intent.getStringExtra("PONTO_HORARIO")

        val imageView: ImageView = findViewById(R.id.imageViewPontoTuristico)

        // Configura os dados na interface
        findViewById<TextView>(R.id.textViewNomeDetalhe).text = nome
        findViewById<TextView>(R.id.textViewInfo1).text = endereco
        findViewById<TextView>(R.id.textViewInfo2).text = avaliacao ?: "Sem avaliações disponíveis"
        findViewById<TextView>(R.id.textViewInfo3).text = horario ?: "Horário não disponível"

        // Configura a lógica do botão "Favoritar"
        val pontoTuristico = FavoriteItem(
            id = "123",
            name = nome ?: "Sem nome",
            description = endereco ?: "Endereço não disponível"
        )

        buttonFavoritar.setOnClickListener {
            favoritesManager.addFavoriteItem(pontoTuristico, {
                Toast.makeText(this, "Adicionado aos favoritos!", Toast.LENGTH_SHORT).show()
            }, { error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            })
        }

        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            imageView.setImageBitmap(bitmap)
        } else {
            imageView.setImageResource(R.drawable.logo_placeholder)
        }

        // Configura o botão de GPS
        findViewById<Button>(R.id.buttonOpenGPS).setOnClickListener {
            val wazeUri = Uri.parse("waze://?ll=$latitude,$longitude&navigate=yes")
            val intent = Intent(Intent.ACTION_VIEW, wazeUri)
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent) // Abrir no Waze
            } else {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://waze.com/ul?ll=$latitude,$longitude&navigate=yes")
                    )
                )
            }
        }

        val sharedPreferences = getSharedPreferences("FAVORITOS", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val favoritoExistente = sharedPreferences.contains(nome)

        buttonFavoritar.text = if (favoritoExistente) "Remover dos Favoritos" else "Adicionar aos Favoritos"

        buttonFavoritar.setOnClickListener {
            val ponto = PontoTuristico(
                nome = nome ?: "Sem nome",
                info1 = endereco ?: "Endereço não disponível",
                info2 = avaliacao ?: "",
                info3 = horario ?: "",
                latitude = latitude,
                longitude = longitude,
                fotoPath = imagePath
            )
            val pontoJson = gson.toJson(ponto)

            if (favoritoExistente) {
                editor.remove(nome)
                Toast.makeText(this, "$nome foi removido dos favoritos!", Toast.LENGTH_SHORT).show()
                buttonFavoritar.text = "Adicionar aos Favoritos"
            } else {
                editor.putString(nome, pontoJson)
                Toast.makeText(this, "$nome foi adicionado aos favoritos!", Toast.LENGTH_SHORT).show()
                buttonFavoritar.text = "Remover dos Favoritos"
            }
            editor.apply()
        }
    }
}

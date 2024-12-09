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
import com.example.myapplication.PontoTuristico
import android.graphics.BitmapFactory

class PontoTuristicoDetalhe : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ponto_turistico_detalhe)

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

        // Carrega a imagem do cache
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
                // Fallback caso o Waze não esteja instalado
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://waze.com/ul?ll=$latitude,$longitude&navigate=yes")
                    )
                )
            }
        }

        // Configura o botão de Favoritar
        val buttonFavoritar = findViewById<Button>(R.id.buttonFavoritar)
        val sharedPreferences = getSharedPreferences("FAVORITOS", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Verifica se o item já está nos favoritos
        val gson = Gson()
        val favoritoExistente = sharedPreferences.contains(nome)

        // Atualiza o texto do botão com base no estado atual
        buttonFavoritar.text = if (favoritoExistente) "Remover dos Favoritos" else "Adicionar aos Favoritos"

        buttonFavoritar.setOnClickListener {
            val ponto = PontoTuristico(
                nome = nome ?: "Sem nome",
                info1 = endereco ?: "Endereço não disponível",
                info2 = avaliacao ?: "",
                info3 = horario ?: "",
                latitude = latitude,
                longitude = longitude,
                fotoPath = imagePath // Salva o caminho da imagem no cache
            )
            val pontoJson = gson.toJson(ponto)

            if (favoritoExistente) {
                // Remove o ponto dos favoritos
                editor.remove(nome)
                Toast.makeText(this, "$nome foi removido dos favoritos!", Toast.LENGTH_SHORT).show()
                buttonFavoritar.text = "Adicionar aos Favoritos"
            } else {
                // Adiciona o ponto aos favoritos
                editor.putString(nome, pontoJson)
                Toast.makeText(this, "$nome foi adicionado aos favoritos!", Toast.LENGTH_SHORT).show()
                buttonFavoritar.text = "Remover dos Favoritos"
            }
            editor.apply() // Aplica as mudanças no SharedPreferences
        }
    }
}


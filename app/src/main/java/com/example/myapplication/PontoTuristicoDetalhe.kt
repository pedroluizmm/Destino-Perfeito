package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import android.widget.Toast
import com.example.myapplication.PontoTuristico

class PontoTuristicoDetalhe : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ponto_turistico_detalhe)

        // Recupera o objeto PontoTuristico passado pela Intent
        val pontoTuristico = intent.getParcelableExtra<PontoTuristico>("PONTO_TURISTICO")

        // Configura os campos de texto
        pontoTuristico?.let {
            findViewById<TextView>(R.id.textViewNomeDetalhe).text = it.nome
            findViewById<TextView>(R.id.textViewInfo1).text = it.info1
            findViewById<TextView>(R.id.textViewInfo2).text = it.info2
            findViewById<TextView>(R.id.textViewInfo3).text = it.info3
        }

        // Configura o botão de GPS
        findViewById<Button>(R.id.buttonOpenGPS).setOnClickListener {
            pontoTuristico?.let {
                val wazeUri = Uri.parse("waze://?ll=${it.latitude},${it.longitude}&navigate=yes")
                val intent = Intent(Intent.ACTION_VIEW, wazeUri)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent) // Abrir no Waze
                } else {
                    // Fallback caso o Waze não esteja instalado
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://waze.com/ul?ll=${it.latitude},${it.longitude}&navigate=yes")
                        )
                    )
                }
            }
        }

        // Configura o botão de Favoritar
        findViewById<Button>(R.id.buttonFavoritar).setOnClickListener {
            pontoTuristico?.let {
                // Salvar o nome e a latitude e longitude do ponto turístico como exemplo
                val sharedPreferences = getSharedPreferences("FAVORITOS", MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                // Aqui você pode salvar as informações como String, por exemplo
                val pontoJson = Gson().toJson(it) // Usando Gson para converter o objeto em JSON
                editor.putString(it.nome, pontoJson)
                editor.apply()

                Toast.makeText(this, "${it.nome} foi adicionado aos favoritos!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}


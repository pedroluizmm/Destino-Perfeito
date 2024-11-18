package com.example.myapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.PontoTuristico

class PontoTuristicoDetalhe : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ponto_turistico_detalhe)

        // Recupera o objeto PontoTuristico passado pela Intent
        val pontoTuristico = intent.getParcelableExtra<PontoTuristico>("PONTO_TURISTICO")

        // Se o ponto turístico for válido, atualiza a UI
        pontoTuristico?.let {
            // Exemplo de como você pode preencher os campos com as informações do ponto turístico
            findViewById<TextView>(R.id.textViewNomeDetalhe).text = it.nome
            findViewById<TextView>(R.id.textViewInfo1).text = it.info1
            findViewById<TextView>(R.id.textViewInfo2).text = it.info2
            findViewById<TextView>(R.id.textViewInfo3).text = it.info3
        }
    }
}



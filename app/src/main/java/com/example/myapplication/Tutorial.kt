package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Tutorial : AppCompatActivity() {
    private lateinit var buttonContinuar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        buttonContinuar = findViewById(R.id.buttonContinuar)

        buttonContinuar.setOnClickListener {
            Log.d("tutorial", "botão clicado")
            val intent = Intent(this, MenuPrincipal::class.java)
            startActivity(intent)
            finish()
        }
    }
}
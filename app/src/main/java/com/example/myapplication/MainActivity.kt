package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextSenha: EditText
    private lateinit var buttonLogin: Button
    private lateinit var textViewCriarConta: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextSenha = findViewById(R.id.editTextSenha)
        buttonLogin = findViewById(R.id.buttonLogin)
        textViewCriarConta = findViewById(R.id.textViewCriarConta)

        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val senha = editTextSenha.text.toString()
            if (email.isNotEmpty() && senha.isNotEmpty()) {
                Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, Tutorial::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        textViewCriarConta.setOnClickListener {
            val intent = Intent(this, CriacaoConta::class.java)
            startActivity(intent)
        }
    }
}
package com.example.myapplication

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Perfil : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnEditarNome: ImageButton
    private lateinit var btnEditarEmail: ImageButton
    private lateinit var btnEditarSenha: ImageButton
    private lateinit var btnConfirmar: Button
    private lateinit var txtNome: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtSenha: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Inicializa os componentes
        btnBack = findViewById(R.id.btnBack)
        btnEditarNome = findViewById(R.id.btnEditarNome)
        btnEditarEmail = findViewById(R.id.btnEditarEmail)
        btnEditarSenha = findViewById(R.id.btnEditarSenha)
        btnConfirmar = findViewById(R.id.btnConfirmar)
        txtNome = findViewById(R.id.txtNome)
        txtEmail = findViewById(R.id.txtEmail)
        txtSenha = findViewById(R.id.txtSenha)

        // Configurações de clique
        btnBack.setOnClickListener {
            finish()
        }

        btnEditarNome.setOnClickListener {
            Toast.makeText(this, "Editar Nome", Toast.LENGTH_SHORT).show()
        }

        btnEditarEmail.setOnClickListener {
            Toast.makeText(this, "Editar Email", Toast.LENGTH_SHORT).show()
        }

        btnEditarSenha.setOnClickListener {
            Toast.makeText(this, "Editar Senha", Toast.LENGTH_SHORT).show()
        }

        btnConfirmar.setOnClickListener {
            Toast.makeText(this, "Informações salvas", Toast.LENGTH_SHORT).show()
        }
    }
}

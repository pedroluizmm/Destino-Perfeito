package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class LoginTabFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_tab, container, false)

        auth = FirebaseAuth.getInstance()

        val emailEditText: EditText = view.findViewById(R.id.login_email)
        val passwordEditText: EditText = view.findViewById(R.id.login_senha)
        val loginButton: Button = view.findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(activity, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(activity, MenuPrincipal::class.java)
                            startActivity(intent)
                            activity?.finish()
                        } else {
                            Toast.makeText(activity, "Erro no login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(activity, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}

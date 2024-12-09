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

class SignupTabFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup_tab, container, false)

        auth = FirebaseAuth.getInstance()

        val emailEditText: EditText = view.findViewById(R.id.signup_email)
        val passwordEditText: EditText = view.findViewById(R.id.signup_senha)
        val confirmPasswordEditText: EditText = view.findViewById(R.id.signup_confirmarSenha)
        val signupButton: Button = view.findViewById(R.id.signup_button)

        signupButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(activity, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(activity, MenuPrincipal::class.java)
                                startActivity(intent)
                                activity?.finish()
                            } else {
                                Toast.makeText(activity, "Erro no cadastro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(activity, "As senhas não coincidem.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}

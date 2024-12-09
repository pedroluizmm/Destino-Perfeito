package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class SignupTabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup_tab, container, false)

        val signupButton: Button = view.findViewById(R.id.signup_button)

        signupButton.setOnClickListener {
            val intent = Intent(activity, MenuPrincipal::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }
}

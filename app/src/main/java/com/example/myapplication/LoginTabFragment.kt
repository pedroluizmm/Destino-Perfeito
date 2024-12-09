package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class LoginTabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_tab, container, false)

        val loginButton: Button = view.findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            val intent = Intent(activity, MenuPrincipal::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }
}

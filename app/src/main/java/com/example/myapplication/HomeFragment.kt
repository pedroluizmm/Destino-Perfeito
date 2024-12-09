package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PontoTuristicoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar o layout do fragmento
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Inicializar o RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Adicionar a lista de pontos turísticos ao adaptador
        val listaPontosTuristicos = PontoTuristicoRepositorio.listaPontosTuristicos
        adapter = PontoTuristicoAdapter(listaPontosTuristicos, requireContext())
        recyclerView.adapter = adapter

        return view
    }
}

package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment(), FiltroAplicavel {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PontoTuristicoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val listaPontosTuristicos = PontoTuristicoRepositorio.listaPontosTuristicos
        adapter = PontoTuristicoAdapter(listaPontosTuristicos, requireContext())
        recyclerView.adapter = adapter

        carregarPontosTuristicos()

        return view
    }

    private fun carregarPontosTuristicos() {
        PontoTuristicoService.buscarPontosTuristicos(requireContext()) {
            requireActivity().runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }
    }
    override fun aplicarFiltro(tipos: List<String>, raio: Int) {
        // Atualiza a lista de pontos turísticos com os filtros
        PontoTuristicoService.buscarPontosTuristicos(requireContext(), raio, tipos) {
            requireActivity().runOnUiThread {
                adapter.notifyDataSetChanged() // Atualiza a RecyclerView
            }
        }
    }

}

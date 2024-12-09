package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MenuPrincipal : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        // Configurando a Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Listener do BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.bottom_lista_fav -> {
                    replaceFragment(ListaFavFragment())
                    true
                }
                R.id.bottom_mapa -> {
                    replaceFragment(MapaFragment())
                    true
                }
                else -> false
            }
        }

        // Carregar o fragmento inicial
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu) // Inflando o menu da Toolbar

        // Configurando a SearchView
        val searchItem = menu?.findItem(R.id.search_topbar)
        val searchView = searchItem?.actionView as? SearchView
        searchView?.queryHint = "Pesquisar..."

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MenuPrincipal, "Busca por: $query", Toast.LENGTH_SHORT).show()
                // Realizar ação de busca
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Atualizar resultados em tempo real (se aplicável)
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.perfil_topbar -> {
                startActivity(Intent(this, Perfil::class.java))
                true
            }
            R.id.filtro_topbar -> {
                Toast.makeText(this, "Ir para Filtro", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.sair_topbar -> {
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(this, "Você saiu da conta.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                true
            }R.id.tema_topbar -> {
                showThemeSelectionDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showThemeSelectionDialog() {
        val options = arrayOf("Tema Claro", "Tema Escuro")
        val selectedTheme = getCurrentTheme() // Função para obter o tema atual

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Escolha o Tema")
        builder.setSingleChoiceItems(options, selectedTheme) { dialog, which ->
            when (which) {
                0 -> setThemeMode(AppCompatDelegate.MODE_NIGHT_NO) // Tema Claro
                1 -> setThemeMode(AppCompatDelegate.MODE_NIGHT_YES) // Tema Escuro
            }
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun setThemeMode(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        saveThemePreference(mode)
    }

    private fun saveThemePreference(mode: Int) {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        sharedPreferences.edit().putInt("theme_mode", mode).apply()
    }

    private fun getCurrentTheme(): Int {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        return sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_NO)
    }

}

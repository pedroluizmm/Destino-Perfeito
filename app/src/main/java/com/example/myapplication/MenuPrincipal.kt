package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
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
                showFilterDialog()
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
    private fun showFilterDialog() {
        // Inflar o layout do diálogo personalizado
        val dialogView = layoutInflater.inflate(R.layout.dialog_filtro, null)
        val checkboxRestaurant = dialogView.findViewById<CheckBox>(R.id.checkbox_restaurant)
        val checkboxCafe = dialogView.findViewById<CheckBox>(R.id.checkbox_cafe)
        val checkboxTouristAttraction = dialogView.findViewById<CheckBox>(R.id.checkbox_tourist_attraction)
        val inputRadius = dialogView.findViewById<EditText>(R.id.input_radius)
        val buttonApplyFilter = dialogView.findViewById<Button>(R.id.button_apply_filter)

        // Criar o diálogo personalizado
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Configurar o clique do botão "Aplicar"
        buttonApplyFilter.setOnClickListener {
            val selectedTypes = mutableListOf<String>()
            if (checkboxRestaurant.isChecked) selectedTypes.add("restaurant")
            if (checkboxCafe.isChecked) selectedTypes.add("cafe")
            if (checkboxTouristAttraction.isChecked) selectedTypes.add("tourist_attraction")

            val radius = inputRadius.text.toString().toIntOrNull() ?: 1000

            // Aplica o filtro no fragmento atual
            aplicarFiltroNoFragment(selectedTypes, radius)

            // Fechar o diálogo após aplicar o filtro
            dialog.dismiss()
        }

        // Mostrar o diálogo
        dialog.show()
    }
    private fun aplicarFiltroNoFragment(tipos: List<String>, raio: Int) {
        val fragment = supportFragmentManager.findFragmentById(R.id.frame_container)

        if (fragment is FiltroAplicavel) {
            fragment.aplicarFiltro(tipos, raio)
        } else {
            Toast.makeText(this, "O filtro não é compatível com o fragmento atual.", Toast.LENGTH_SHORT).show()
        }
    }
}

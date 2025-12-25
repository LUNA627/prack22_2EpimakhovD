package com.example.poke

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var editTextPokemon: EditText
    private lateinit var buttonFetch: Button
    private lateinit var imagePokemon: ImageView
    private lateinit var textResult: TextView
    private lateinit var buttonAllPokemon: Button

    private val prefs by lazy { getSharedPreferences("saved_pokemons", Context.MODE_PRIVATE) }
    private val gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        initViews()
        setupClick()

    }

    private fun initViews() {
        editTextPokemon = findViewById(R.id.edit_text_name)
        buttonFetch = findViewById(R.id.buttonFetch)
        imagePokemon = findViewById(R.id.imagePokemon)
        textResult = findViewById(R.id.textResult)
        buttonAllPokemon = findViewById(R.id.buttonAllPokemon)
    }


    private fun setupClick() {
        buttonFetch.setOnClickListener {
            val query = editTextPokemon.text.toString().trim()
            if (query.isEmpty()) {
                Toast.makeText(this, "Введите имя или ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            fetchPokemon(query)
        }

        buttonAllPokemon.setOnClickListener {
            val intent = Intent(this, AllPokemonActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchPokemon(nameOrId: String) {
        lifecycleScope.launch {
            try {
                val pokemon = RetrofitClient.apiService.getPokemon(nameOrId)
                displayPokemon(pokemon)
                savePokemonToJson(pokemon)
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Покемон не найден", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun displayPokemon(pokemon: Pokemon) {
        val imageUrl = pokemon.sprites.front_default
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this).load(imageUrl).into(imagePokemon)
            imagePokemon.visibility = View.VISIBLE
        }

        val types = pokemon.types.joinToString(", ") { it.type.name }
        val text = """
            Имя: ${pokemon.name.capitalize()}
            ID: ${pokemon.id}
            Рост: ${pokemon.height} дм
            Вес: ${pokemon.weight} г
            Опыт: ${pokemon.base_experience}
            Тип(ы): $types
        """.trimIndent()

        textResult.text = text
        textResult.visibility = View.VISIBLE
    }

    private fun savePokemonToJson(pokemon: Pokemon) {
        val json = gson.toJson(pokemon)
        // Сохраняем по ключу, например, по ID
        prefs.edit().putString("pokemon_${pokemon.id}", json).apply()
    }

}
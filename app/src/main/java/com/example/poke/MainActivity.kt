package com.example.poke

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlin.collections.joinToString


class MainActivity : AppCompatActivity() {

    private lateinit var editTextPokemon: EditText
    private lateinit var buttonFetch: Button
    private lateinit var imagePokemon: ImageView
    private lateinit var textResult: TextView
    private lateinit var buttonAllPokemon: Button

    private val prefs by lazy { getSharedPreferences("saved_pokemons", Context.MODE_PRIVATE) }
    private val gson = Gson()

    private lateinit var db: PokemonDatabase
    private lateinit var dao: PokemonDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        db = PokemonDatabase.getDatabase(this)
        dao = db.pokemonDao()

        initViews()
        setupClick()

    }

    private fun fetchPokemon(nameOrId: String) {
        lifecycleScope.launch {
            try {
                val pokemon = RetrofitClient.apiService.getPokemon(nameOrId)
                displayPokemon(pokemon)

                // Сохраняем в Room (вместо SharedPreferences)
                dao.insertPokemon(pokemon)

            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Покемон не найден", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
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


    private fun savePokemonToDb(pokemon: Pokemon) {
        lifecycleScope.launch {
            val typesString = pokemon.types.joinToString(",") { it.type.name }
            val entity = Pokemon(
                id = pokemon.id,
                name = pokemon.name,
                height = pokemon.height,
                weight = pokemon.weight,
                base_experience = pokemon.base_experience,
                types = typesString,
                spriteUrl = pokemon.sprites.front_default
            )
            val db = AppDatabase.getDatabase(this@MainActivity)
            db.pokemonDao().insert(entity)
        }
    }

}
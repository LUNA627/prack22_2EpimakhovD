package com.example.poke

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.launch

class AllPokemonActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonListAdapter
    private val savedPokemons = mutableListOf<Pokemon>()

    private val prefs by lazy { getSharedPreferences("saved_pokemons", Context.MODE_PRIVATE) }
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_pokemon)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = PokemonListAdapter(savedPokemons) { pokemon ->
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadSavedPokemons()
    }

    private fun loadSavedPokemons() {
        savedPokemons.clear()
        val keys = prefs.all.keys
        for (key in keys) {
            if (key.startsWith("pokemon_")) {
                val json = prefs.getString(key, null)
                if (json != null) {
                    try {
                        val pokemon = gson.fromJson(json, Pokemon::class.java)
                        savedPokemons.add(pokemon)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        savedPokemons.sortBy { it.id }
        adapter.notifyDataSetChanged()
    }
}
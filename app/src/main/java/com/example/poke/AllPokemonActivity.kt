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

    private val gson = Gson()
    private lateinit var db: PokemonDatabase
    private lateinit var dao: PokemonDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_pokemon)

        db = PokemonDatabase.getDatabase(this)
        dao = db.pokemonDao()

        recyclerView = findViewById(R.id.recyclerView)
        // Передаём пустой список — данные придут через Flow
        adapter = PokemonListAdapter(mutableListOf()) { /* можно открыть детали */ }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Подписываемся на изменения в базе
        lifecycleScope.launch {
            lifecycleScope.launch {
                dao.getAllPokemons().collect { pokemons ->
                    adapter.updateList(pokemons)
                }
            }
        }
    }
}
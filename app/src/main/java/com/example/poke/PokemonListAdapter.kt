package com.example.poke

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PokemonListAdapter(
    private val pokemons: List<Pokemon>,
    private val onItemClick: (Pokemon) -> Unit
) : RecyclerView.Adapter<PokemonListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemons[position]
        holder.nameText.text = "#${pokemon.id} ${pokemon.name.replaceFirstChar { it.uppercase() }}"
        holder.itemView.setOnClickListener { onItemClick(pokemon) }
    }

    override fun getItemCount() = pokemons.size
}
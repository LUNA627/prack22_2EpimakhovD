package com.example.poke

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_pokemons")
data class Pokemon(
    @PrimaryKey val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val base_experience: Int,
    val types: String, // можно хранить как CSV или JSON
    val spriteUrl: String?
)
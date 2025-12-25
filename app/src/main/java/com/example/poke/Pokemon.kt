package com.example.poke

data class Pokemon (
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val base_experience: Int,
    val types: List<TypeSlot>,
    val sprites: Sprites
)

data class TypeSlot(
    val type: Type
)

data class Type(
    val name: String
)

data class Sprites(
    val front_default: String?
)
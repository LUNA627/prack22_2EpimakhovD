package com.example.poke

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PokemonTypeConverters {

    private val gson = Gson()

    // List<TypeSlot> → String (JSON)
    @TypeConverter
    fun fromTypeSlotList(types: List<TypeSlot>?): String? {
        return types?.let { gson.toJson(it) }
    }

    // String (JSON) → List<TypeSlot>
    @TypeConverter
    fun toTypeSlotList(json: String?): List<TypeSlot>? {
        if (json.isNullOrEmpty()) return null
        val listType = object : TypeToken<List<TypeSlot>>() {}.type
        return gson.fromJson(json, listType)
    }

    // Sprites → String (JSON)
    @TypeConverter
    fun fromSprites(sprites: Sprites?): String? {
        return sprites?.let { gson.toJson(it) }
    }

    // String (JSON) → Sprites
    @TypeConverter
    fun toSprites(json: String?): Sprites? {
        if (json.isNullOrEmpty()) return null
        return gson.fromJson(json, Sprites::class.java)
    }
}
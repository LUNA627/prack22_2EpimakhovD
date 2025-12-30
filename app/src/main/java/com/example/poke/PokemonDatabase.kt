package com.example.poke

import android.content.Context
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [Pokemon::class],
    version = 1,
    exportSchema = false
)
// Подключаем конвертеры ко ВСЕЙ базе
@TypeConverters(PokemonTypeConverters::class)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao

    companion object {
        @Volatile
        private var INSTANCE: PokemonDatabase? = null

        fun getDatabase(context: Context): PokemonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDatabase::class.java,
                    "pokemon_db"  // имя файла базы
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


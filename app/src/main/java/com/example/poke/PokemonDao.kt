package com.example.poke

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    // Получить ВСЕХ покемонов, отсортированных по ID
    @Query("SELECT * FROM saved_pokemons ORDER BY id ASC")
    fun getAllPokemons(): Flow<List<Pokemon>>

    // Сохранить одного покемона (если уже есть — заменит)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: Pokemon)

    // Проверить, существует ли покемон по ID
    @Query("SELECT EXISTS(SELECT 1 FROM saved_pokemons WHERE id = :id)")
    suspend fun exists(id: Int): Boolean

    // Удалить покемона
    @Delete
    suspend fun deletePokemon(pokemon: Pokemon)

    // Получить по ID (если нужно)
    @Query("SELECT * FROM saved_pokemons WHERE id = :id")
    suspend fun getPokemonById(id: Int): Pokemon?
}

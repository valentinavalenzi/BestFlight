package com.example.bestflight.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CardDAO {
    @Insert
    suspend fun insert(card: Card)
    @Update
    suspend fun update(card: Card)
    @Delete
    suspend fun delete(card: Card)
    @Query("SELECT * FROM Card")
    fun getAllCards(): LiveData<List<Card>>
    @Query("SELECT * FROM Card WHERE id = :cardId LIMIT 1")
    suspend fun getCardById(cardId: Int): Card?
}
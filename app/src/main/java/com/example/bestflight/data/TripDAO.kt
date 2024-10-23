package com.example.bestflight.data

import androidx.room.Dao
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TripDAO {
    @Insert
    suspend fun insert(trip: Trip)
    @Update
    suspend fun update(trip: Trip)
    @Delete
    suspend fun delete(trip: Trip)
    @Query("SELECT * FROM Trip")
    fun getMyTrips(): LiveData<List<Trip>>
}
package com.example.bestflight.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [Trip::class], version = 1)
abstract class BestFlightDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDAO
    companion object {
        @Volatile
        private var INSTANCE: BestFlightDatabase? = null
        fun getDatabase(context: Context): BestFlightDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BestFlightDatabase::class.java,
                    "bestflight_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
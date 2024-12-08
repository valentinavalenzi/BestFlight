package com.example.bestflight.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `Card` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `cardNumber` TEXT NOT NULL,
                `expirationDate` TEXT NOT NULL,
                `cvv` TEXT NOT NULL,
                `cardType` TEXT NOT NULL
            )
        """)
    }
}

@Database(entities = [Trip::class, Card::class], version = 2)
abstract class BestFlightDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDAO
    abstract fun cardDao(): CardDAO
    companion object {
        @Volatile
        private var INSTANCE: BestFlightDatabase? = null
        fun getDatabase(context: Context): BestFlightDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BestFlightDatabase::class.java,
                    "bestflight_database"
                )
                    .addMigrations(MIGRATION_1_2) // Add the migration here
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}
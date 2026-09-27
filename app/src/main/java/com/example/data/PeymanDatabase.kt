package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DelayProjectEntity::class], version = 1, exportSchema = false)
abstract class PeymanDatabase : RoomDatabase() {
    abstract fun delayProjectDao(): DelayProjectDao

    companion object {
        @Volatile
        private var INSTANCE: PeymanDatabase? = null

        fun getDatabase(context: Context): PeymanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PeymanDatabase::class.java,
                    "peyman_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

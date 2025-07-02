package com.example.projectbeta.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projectbeta.data.local.dao.FoodDao
import com.example.projectbeta.data.local.entity.FoodEntity

@Database(entities = [FoodEntity::class], version = 2) // Naikkan version dari 1 ke 2
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "food_database"
                )
                    .fallbackToDestructiveMigration() // Tambahkan ini untuk menghapus dan membuat ulang database
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
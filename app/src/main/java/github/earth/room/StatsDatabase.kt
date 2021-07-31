package com.example.room.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//сама БД
@Database(entities = [StatsRoom::class], version = 1, exportSchema = false)
abstract class StatsDatabase : RoomDatabase() {

    abstract fun statsDao(): StatsDao

    companion object {
        @Volatile
        private var INSTANCE: StatsDatabase? = null

        fun getDatabase(context: Context): StatsDatabase {
            val tempInstance = INSTANCE
            //если есть возвращаем
            if (tempInstance != null) {
                return tempInstance
            }
            //если нет, создаём
            //в фоновом потоке
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StatsDatabase::class.java,
                    "stats_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}
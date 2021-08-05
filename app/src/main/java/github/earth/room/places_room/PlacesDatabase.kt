package github.earth.room.data.places_data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PlacesRoom::class], version = 1, exportSchema = false)
abstract class PlacesDatabase : RoomDatabase() {

    abstract fun placesDao(): PlacesDao

    companion object {
        @Volatile
        private var INSTANCE: PlacesDatabase? = null

        fun getDatabase(context: Context): PlacesDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlacesDatabase::class.java,
                    "places_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
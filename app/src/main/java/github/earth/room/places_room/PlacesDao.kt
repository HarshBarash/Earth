package github.earth.room.data.places_data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlacesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlaces(places: PlacesRoom)

    @Update
    suspend fun updatePlaces(places: PlacesRoom)

    @Delete
    suspend fun deletePlaces(places: PlacesRoom)

    @Query("DELETE FROM places_table")
    suspend fun deleteAllPlaces()

    @Transaction
    @Query("SELECT * FROM places_table ORDER BY id ASC")
    fun readAllPlaces(): LiveData<List<PlacesRoom>>

}
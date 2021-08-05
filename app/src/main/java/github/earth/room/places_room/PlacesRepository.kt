package github.earth.room.data.places_data

import androidx.lifecycle.LiveData

class PlacesRepository(private val placesDao: PlacesDao) {

    val readAllData: LiveData<List<PlacesRoom>> = placesDao.readAllPlaces()

    suspend fun addPlaces(places: PlacesRoom) {
        placesDao.addPlaces(places)
    }

    suspend fun updatePlaces(places: PlacesRoom) {
        placesDao.updatePlaces(places)
    }

    suspend fun deletePlaces(places: PlacesRoom) {
        placesDao.deletePlaces(places)
    }

    suspend fun deleteAllPlaces() {
        placesDao.deleteAllPlaces()
    }


}
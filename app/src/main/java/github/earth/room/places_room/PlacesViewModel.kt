package github.earth.room.places_room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import github.earth.room.data.places_data.PlacesDatabase
import github.earth.room.data.places_data.PlacesRepository
import github.earth.room.data.places_data.PlacesRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlacesViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<PlacesRoom>>
    private val repository: PlacesRepository

    init {
        val placesDao = PlacesDatabase.getDatabase(
            application
        ).placesDao()
        repository = PlacesRepository(placesDao)
        readAllData = repository.readAllData
    }

    fun addPlaces(places: PlacesRoom) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPlaces(places)
        }
    }

    fun updatePlaces(places: PlacesRoom) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePlaces(places)
        }
    }

    fun deletePlaces(places: PlacesRoom) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePlaces(places)
        }
    }

    fun deleteAllPlaces() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllPlaces()
        }
    }


}
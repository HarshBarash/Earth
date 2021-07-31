package com.example.room.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.room.data.StatsDatabase
import com.example.room.data.StatsRoom
import com.example.room.repository.StatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//передает изменения в UI (связь между repository и ui)
class StatsViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<StatsRoom>>
    private val repository: StatsRepository

    //инициализация бд
    init {
        val statsDao = StatsDatabase.getDatabase(
            application
        ).statsDao()
        repository = StatsRepository(statsDao)
        readAllData = repository.readAllData
    }

    fun addStats(stats: StatsRoom){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addStats(stats)
        }
    }

    fun updateStats(stats: StatsRoom){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateStats(stats)
        }
    }

    fun deleteStats(stats: StatsRoom){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteStats(stats)
        }
    }

    fun deleteAllStats(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllStats()
        }
    }


}
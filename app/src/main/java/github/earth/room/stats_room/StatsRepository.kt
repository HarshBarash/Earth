package github.earth.room.stats_room

import androidx.lifecycle.LiveData

//методы для работы с данными в разных источниках.
class StatsRepository(private val statsDao: StatsDao) {

    val readAllData: LiveData<List<StatsRoom>> = statsDao.readAllData()

    suspend fun addStats(stats: StatsRoom){
        statsDao.addStats(stats)
    }

    suspend fun updateStats(stats: StatsRoom){
        statsDao.updateStats(stats)
    }

    suspend fun deleteStats(stats: StatsRoom){
        statsDao.deleteStats(stats)
    }

    suspend fun deleteAllStats(){
        statsDao.deleteAllStats()
    }


}

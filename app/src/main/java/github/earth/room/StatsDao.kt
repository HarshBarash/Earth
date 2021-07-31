package com.example.room.data

import androidx.lifecycle.LiveData
import androidx.room.*

//методы для работы с БД
@Dao
interface StatsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addStats(stats: StatsRoom)

    @Update
    suspend fun updateStats(stats: StatsRoom)

    @Delete
    suspend fun deleteStats(stats: StatsRoom)

    @Query("DELETE FROM stats_table")
    suspend fun deleteAllStats()

    @Query("SELECT * FROM stats_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<StatsRoom>>
}
package com.example.room.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//таблица с данными.

@Parcelize
@Entity(tableName = "stats_table")
data class StatsRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val collected_waste: Int,
    val visited_places: Int,
    val rank: Int
) : Parcelable

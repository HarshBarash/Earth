package github.earth.room.data.places_data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "places_table")
data class PlacesRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val lattitude: Double,
    val longtitude: Double
) : Parcelable
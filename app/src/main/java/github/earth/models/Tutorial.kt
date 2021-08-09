package github.earth.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tutorial(
    val title: String = "",
    val materials: String = "",
    val description: String = "",
    val time: Int = 1,
    val tutorialImageUrl: String = "",
    val username: String = "",
    val profileImageUrl: String = "",
    val timestamp: Long = -1
) : Parcelable


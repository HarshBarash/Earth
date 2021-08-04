package github.earth.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.ServerValue
import java.util.*

data class Feed(val uid: String = "", val username: String = "",
                val image: String = "", val title: String = "",
                val timestamp: Any = ServerValue.TIMESTAMP, val photo: String? = null,
                @Exclude val id: String = "", val tutorial: String = "", val materals: String ="",
                val level: String="", val time: Int = 1, val likesCount: Int = 0) {
    fun timestampDate(): Date = Date(timestamp as Long)
}
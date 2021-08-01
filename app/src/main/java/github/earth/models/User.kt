package github.earth.models

import com.google.firebase.database.Exclude

data class User(
    val email: String ="",
    val username: String ="",
    val photo: String? = null,
    @Exclude val uid: String = ""
)
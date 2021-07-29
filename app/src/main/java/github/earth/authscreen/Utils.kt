package github.earth.authscreen

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import github.earth.utils.LOG_VEL

class ValueEventListenerAdapter (val handler: (DataSnapshot) -> Unit) : ValueEventListener {
    override fun onDataChange(data: DataSnapshot) {
        handler(data)
    }

    override fun onCancelled(error: DatabaseError) {
        Log.e(LOG_VEL, "onCancelled", error.toException())
    }
}

// наследник всех классов контекста -> активити
fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT ){
    Toast.makeText(this, text, duration).show()
}
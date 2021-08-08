package github.earth.markers_fb_realtime

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.Marker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

abstract class MarkersItemData {
    private val TAG = "ReadAndWriteMarkers"

    private lateinit var database: DatabaseReference

    fun initializeDbRef() {
        // [START initialize_database_ref]
        database = Firebase.database.reference
        // [END initialize_database_ref]
    }

    fun writeNewMarker(markerId: String, latitude: Double, longitude: Double) {
        val marker = Marker(latitude, longitude)

        database.child("markers").child(markerId).setValue(marker)
    }

    fun writeNewMarkerWithTaskListeners(markerId: String, latitude: Double, longitude: Double) {
        val marker = Marker(latitude, longitude)

        database.child("markers").child(markerId).setValue(marker)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }

}

data class Marker(val latitude: Double? = null, val longitude: Double? = null)

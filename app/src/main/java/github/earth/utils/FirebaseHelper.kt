package github.earth.utils

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask


class FirebaseHelper(private val activity: FragmentActivity?) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    val storage: StorageReference =  FirebaseStorage.getInstance().reference


  
    
    fun updateUser(updates: Map<String, Any?>, onSuccess: () -> Unit) {
        database.child("users").child(currentUid()!!).updateChildren(updates)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        onSuccess()
                    } else {
                        activity?.showToast(it.exception!!.message!!)
                    }
                }
    }

    fun updateEmail(email: String, onSuccess: () -> Unit) {
        auth.currentUser!!.updateEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                it.exception!!.message!!
                activity?.showToast(it.exception!!.message!!)
            }
        }
    }

    fun reauthenticate(credential: AuthCredential, onSuccess: () -> Unit) {
        auth.currentUser!!.reauthenticate(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                if (activity != null) {
                    activity.showToast(it.exception!!.message!!)
                }
            }
        }
    }

    fun uploadSharePhoto(localPhotoUrl: Uri, onSuccess: (UploadTask.TaskSnapshot) -> Unit) =
        storage.child("users/${auth.currentUser!!.uid}").child("images")
            .child(localPhotoUrl.lastPathSegment!!)
            .putFile(localPhotoUrl)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    onSuccess(it.result!!)
                else
                    if (activity != null) {
                        activity.showToast(it.exception!!.message!!)
                    }
            }

    fun addSharePhoto(globalPhotoUrl: String, onSuccess: () -> Unit) =
        database.child("images").child(auth.currentUser!!.uid)
            .push().setValue(globalPhotoUrl)
            .addOnComplete { onSuccess() }

    private fun Task<Void>.addOnComplete(onSuccess: () -> Unit) {
        addOnCompleteListener {
            if (it.isSuccessful)
                onSuccess()
            else
                if (activity != null) {
                    activity.showToast(it.exception!!.message!!)
                }
        }
    }

    fun currentUserReference(): DatabaseReference =
            database.child("users").child(currentUid()!!)

    fun currentUid(): String? =
            auth.currentUser?.uid

}


fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT ){
    Toast.makeText(this, text, duration).show()
}
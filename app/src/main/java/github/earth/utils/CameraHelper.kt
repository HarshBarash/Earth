package github.earth.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraHelper(private val activity: Activity) {

//    private val PICK_IMAGE_REQUEST = 71
//    private var filePath: Uri? = null
//    private var firebaseStore: FirebaseStorage? = null
//    private var storageReference: StorageReference? = null
//
//    fun takeCameraPicture() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (intent.resolveActivity(activity.packageManager) != null) {
//            val imageFile = createImageFile()
//            imageUri = FileProvider.getUriForFile(activity,
//                    "github.earth.fileprovider",
//                    imageFile)
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
//            activity.startActivityForResult(intent, REQUEST_CODE)
//        }
//    }
//
//    private fun createImageFile(): File {
//        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return File.createTempFile(
//                "JPEG_${simpleDateFormat.format(Date())}_",
//                ".jpg",
//                storageDir
//        )
//    }

}
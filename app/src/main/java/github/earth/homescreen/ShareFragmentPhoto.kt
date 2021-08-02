package github.earth.homescreen

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.database.ServerValue
import github.earth.R
import github.earth.models.User
import github.earth.utils.CameraHelper
import github.earth.utils.FirebaseHelper
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import github.earth.utils.ValueEventListenerAdapter
import kotlinx.android.synthetic.main.fragment_sharephoto.view.*


class ShareFragmentPhoto  : Fragment() {

    private lateinit var mCameraHelper: CameraHelper
    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var mUser : User
    private var mImageUri: String? = null

    var imageUri: Uri? = null

    private val simpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(github.earth.R.layout.fragment_sharephoto, container, false)

        val imageView: ImageView = view.findViewById(github.earth.R.id.tutorial_image)

        mFirebaseHelper = FirebaseHelper(activity)
        mCameraHelper = CameraHelper(requireActivity())
//        mCameraHelper.takeCameraPicture()

//        view.continuebitn.setOnClickListener { share() }


//        Glide.with(this).load(mCameraHelper.imageUri).centerCrop().into(imageView)



        mFirebaseHelper.currentUserReference().addValueEventListener(ValueEventListenerAdapter {
            it.getValue(User::class.java)!!
        })
        return view
    }

//    private fun share() {
//        val imageUri = mCameraHelper.imageUri
//        if (imageUri != null) {
//            val uid = mFirebaseHelper.auth.currentUser!!.uid
//            mFirebaseHelper.uploadSharePhoto(imageUri) {
//                val imageDownloadUrl = it.metadata!!.reference!!.downloadUrl.toString()
//                 it.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                    mFirebaseHelper.database.child("Feed").child(uid)
//                        .push().setValue(mkFeed(uid, imageDownloadUrl)).addOnCompleteListener {
//                            mFirebaseHelper.addSharePhoto(it.toString()) {
//                                if (it.isSuccessful) {
//                                    findNavController().navigate(R.id.action_SharePhotoScreen_to_HomeFragment)
//                                }
//                        }
//                    }
//                }
//            }
//        }
//    }

    private fun mkFeed(uid: String, imageDownloadUrl: String) = Feed(
        uid = uid,
        image = imageDownloadUrl,
    //                            title = title.text.toString()
    )


//    private fun share() {
//        val imageUri = mCameraHelper.imageUri
//        if (imageUri != null) {
//            mFirebaseHelper.uploadSharePhoto(imageUri) {
//                val imageDownloadUrl = it.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                }
//                mFirebaseHelper.addSharePhoto(it.toString()) {
//                    mFirebaseHelper.database.child("feed")
//                        .child(mFirebaseHelper.auth.currentUser!!.uid).push()
//                        .setValue(
//                            Feed(
//                                uid = mFirebaseHelper.auth.currentUser!!.uid, //uid
//                                image = imageDownloadUrl.toString()
//                                // title сюда захвачу предыдушие фрагменты
//                }
//                            )
//                        )
//                }
//            }
//        }
//    }

//занести spinner

            data class Feed(
                val uid: String = "", val username: String = "", val image: String = "",
                val title: String = "", val tutorial: String = "", val materals: String ="",
                val level: String="", val time: Int = 1, val likesCount: Int = 0,
                val timestamp: Any = ServerValue.TIMESTAMP
            ) {

                fun fimestampDate(): Date = Date(timestamp as Long)
            }
}





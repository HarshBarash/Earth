package github.earth.homescreen

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import github.earth.R
import github.earth.models.User
import github.earth.utils.CameraHelper
import github.earth.utils.FirebaseHelper

class ShareLinkFragment : Fragment() {

    private lateinit var mCameraHelper: CameraHelper
    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var mUser : User
    private var mImageUri: String? = null

    var imageUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sharelink, container, false)

        return view
    }

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
//                            ShareFragmentPhoto.Feed(
//                                uid = mFirebaseHelper.auth.currentUser!!.uid, //uid
//                                image = imageDownloadUrl.toString()
//                    // title сюда захвачу предыдушие фрагменты
//                }
//                )
//                )
//            }
//        }
//    }
//}
}
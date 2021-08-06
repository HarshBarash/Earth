package github.earth.homescreen

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.DialogTitle
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import github.earth.R
import github.earth.models.Feed
import github.earth.models.User
import github.earth.utils.*
import kotlinx.android.synthetic.main.fragment_shareinfo.*
import kotlinx.android.synthetic.main.fragment_sharelink.*

class ShareLinkFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    private lateinit var mCameraHelper: CameraHelper
    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(LOG_PROFILE_FRAGMENT, "onCreate called")

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mCameraHelper = CameraHelper(requireActivity())
        mFirebaseHelper = FirebaseHelper(requireActivity())


        mFirebaseHelper.currentUserReference().addValueEventListener(ValueEventListenerAdapter {
            mUser = it.asUser()!!
        })


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sharelink, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val image = arguments?.getString("ImageUri")
//        val title = arguments?.getString("Title")
//        val materials = arguments?.getString("Matrials")
//        val time = arguments?.getString("Time")


        publish.setOnClickListener {
        share()
        }
    }


    private fun share() {
        val image = arguments?.getString("ImageUri")
        val title = arguments?.getString("Title")
        val materials = arguments?.getString("Materials")
        val time = arguments?.getInt("Time")

        val tutorial = etTutorial.text.toString()
        val link = etLink.text.toString()
        if (image != null && title != null && materials != null && time != null && tutorial != null) {

            val uid = mFirebaseHelper.currentUid()!!
            mFirebaseHelper.uploadSharePhoto(image.toString().toUri()) {
                val imageDownloadUrl = it.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                }
                mFirebaseHelper.addSharePhoto(it.toString()) {
                    mFirebaseHelper.database.child("feed")
                        .child(mFirebaseHelper.auth.currentUser!!.uid).push()
                        .setValue(
                            mkFeed(uid, image.toString(), link, title, materials, time )).addOnCompleteListener({
                            findNavController().navigate(R.id.action_shareLinkFragment_to_HomeFragment)
                        })
                }

            }
        }
    }

    private fun mkFeed(uid: String, imageDownloadUrl: String, link: String, title: String, materials: String, time: Int) : Feed {
        return Feed(
            uid = uid,
            username = mUser.username,
            photo = mUser.photo,
            image = imageDownloadUrl,
            link = etTutorial.text.toString(),
            tutorial = etTutorial.text.toString(),
            title = title,
            materals = materials,
            time = time

        )
    }
}
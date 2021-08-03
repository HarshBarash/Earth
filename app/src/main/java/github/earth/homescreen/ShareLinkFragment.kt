package github.earth.homescreen

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import github.earth.R
import github.earth.models.Feed
import github.earth.models.User
import github.earth.utils.*
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

        val image = arguments?.getString("ImageUri")
        val title = arguments?.getString("Title")
        val materials = arguments?.getString("Matrials")
        val time = arguments?.getString("Time")


        publish.setOnClickListener {
        share()
        }
    }


    private fun share() {
        val imageUri = mCameraHelper.imageUri
        if (imageUri != null) {
            mFirebaseHelper.uploadSharePhoto(imageUri) {
                val imageDownloadUrl = it.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                }
                mFirebaseHelper.addSharePhoto(it.toString()) {
                    mFirebaseHelper.database.child("feed")
                        .child(mFirebaseHelper.auth.currentUser!!.uid).push()
                        .setValue(
                            Feed(
                                uid = mFirebaseHelper.auth.currentUser!!.uid, //uid
                                image = imageDownloadUrl.toString()
                                // title сюда захвачу предыдушие фрагменты

                            )
                        )
                }
            }
            }
        }
    }
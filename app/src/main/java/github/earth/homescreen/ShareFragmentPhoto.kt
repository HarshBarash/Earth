package github.earth.homescreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import github.earth.R
import github.earth.models.User
import github.earth.utils.*
import kotlinx.android.synthetic.main.fragment_sharephoto.*
import kotlinx.android.synthetic.main.fragment_sharephoto.view.*


class ShareFragmentPhoto  : Fragment() {


    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    private lateinit var mCameraHelper: CameraHelper
    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(LOG_SHARE_PHOTO_FRAGMENT, "onCreate called")

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mFirebaseHelper = FirebaseHelper(activity)

        checkandGetpermissions()

        mCameraHelper = CameraHelper(requireActivity())


        mFirebaseHelper.currentUserReference().addValueEventListener(ValueEventListenerAdapter {
            mUser = it.asUser()!!
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(LOG_SORTING_FRAGMENT, "onCreateView called")
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_sharephoto, container, false)

        val imageButton: ImageButton = rootView.findViewById(R.id.ivTutorialImageShare)
        val bundle = Bundle()


        imageButton.setOnClickListener({
            mCameraHelper.takeCameraPicture()

            Glide.with(this).load(mCameraHelper.imageUri).centerCrop()
                .transform(
                    CenterCrop(),
                    RoundedCorners(15)
                ).into(imageButton)
        })

        rootView.nextbuttonone.setOnClickListener({
            val ImageUri = mCameraHelper.imageUri
            if (ImageUri != null) {
                bundle.putString("ImageUri", ImageUri.toString())
                findNavController().navigate(R.id.action_SharePhotoScreen_to_shareInfoFragment, bundle)
            } else {
                requireActivity().showToast("Please make photo")
            }
        })

        return rootView
    }


@SuppressLint("WrongConstant")
public fun checkandGetpermissions() {
    if (PermissionChecker.checkSelfPermission(
            requireActivity(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_DENIED
    ) {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
    } else {
        requireActivity().showToast("Camera permission granted")
    }
}

override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == 100) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requireActivity().showToast("Camera permission granted")
        } else {

            requireActivity().showToast("Permission Denied")
        }
    }
}
}


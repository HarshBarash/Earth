package github.earth.homescreen

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import github.earth.R
import github.earth.models.User
import github.earth.utils.CameraHelper
import github.earth.utils.FirebaseHelper
import github.earth.utils.ValueEventListenerAdapter
import java.text.SimpleDateFormat
import java.util.*

class ShareInfoFragment : Fragment() {

    private lateinit var mCameraHelper: CameraHelper
//    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var mUser : User
    private var mImageUri: String? = null

    var imageUri: Uri? = null

    private val simpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(github.earth.R.layout.fragment_sharephoto, container, false)

//        mFirebaseHelper = FirebaseHelper(activity)
//
//        mFirebaseHelper.currentUserReference().addValueEventListener(ValueEventListenerAdapter {
//            it.getValue(User::class.java)!!
//        })
        return view
    }

//    fun onNext(image: String, title: String, spinnerL: Spinner, spinnerM: Spinner, time :Int ) {
//        if (mCameraHelper.imageUri != null) {
//            mImageUri = mCameraHelper.imageUri.toString()
//            findNavController().navigate(R.id.action_SharePhotoScreen_to_shareInfoFragment)
//        } else {
//            Toast.makeText(requireView().context , "Please add a photo", Toast.LENGTH_SHORT).show()
//        }
//    }

}
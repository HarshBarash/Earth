package github.earth.homescreen

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import github.earth.R
import github.earth.utils.CameraHelper
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_sharephoto.*

class ShareFragmentPhoto  : Fragment() {

    private lateinit var mCameraHelper: CameraHelper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sharephoto, container, false)


        mCameraHelper = activity?.let { CameraHelper(it) }!!
        mCameraHelper.takeCameraPicture()



        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == mCameraHelper.REQUEST_CODE && resultCode == RESULT_OK ) {
            Glide.with(this).load(mCameraHelper.imageUri).centerCrop().into(post_image)
        }
    }
}
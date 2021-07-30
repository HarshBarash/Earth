package github.earth.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import github.earth.R
import github.earth.utils.CameraHelper
import kotlinx.android.synthetic.main.fragment_profile.*

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
}
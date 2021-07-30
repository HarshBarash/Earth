package github.earth.homescreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import github.earth.R
import github.earth.authscreen.ValueEventListenerAdapter
import github.earth.models.User
import github.earth.utils.FirebaseHelper
import github.earth.utils.LOG_PROFILE_FRAGMENT
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var mUser : User


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        Log.d(LOG_PROFILE_FRAGMENT, "onCreate")

        mFirebaseHelper = FirebaseHelper(activity)
        mFirebaseHelper.currentUserReference().addValueEventListener(ValueEventListenerAdapter {
            mUser = it.getValue(User::class.java)!!
            username_text.text = mUser.username


        })
        return view
    }
}
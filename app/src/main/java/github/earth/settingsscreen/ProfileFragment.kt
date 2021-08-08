package github.earth.settingsscreen

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import github.earth.R
import github.earth.models.User
import github.earth.utils.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import github.earth.MainActivity
import kotlinx.android.synthetic.main.fragment_userchange.*


class ProfileFragment : Fragment() {

    private lateinit var mCameraHelper: CameraHelper
    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var mUser : User
    private lateinit var mPendingUser: User
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    private val currentUser = FirebaseAuth.getInstance().currentUser

    private lateinit var fabSettings : FloatingActionButton

    private lateinit var toChangeOne: TextView
    private lateinit var toChangeTwo: ImageView
    private lateinit var topAppBar: com.google.android.material.appbar.MaterialToolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(LOG_PROFILE_FRAGMENT, "onCreate called")

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mFirebaseHelper = FirebaseHelper(activity)

        mCameraHelper = CameraHelper(requireActivity())


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        Log.d(LOG_PROFILE_FRAGMENT, "onCreateView called")

        toChangeOne = view.findViewById(R.id.username_text)
        toChangeTwo = view.findViewById(R.id.user_photo)
        fabSettings = view.findViewById(R.id.fabSettings)

        // TODO: 02.08.2021 привести  все и зарефакторить
        currentUser?.let { user ->
            Glide.with(this)
                .load(user.photoUrl)
                .circleCrop()
                .into(toChangeTwo)
            mFirebaseHelper.currentUserReference().addValueEventListener(ValueEventListenerAdapter {
            mUser = it.getValue(User::class.java)!!
            view.username_text.text = mUser.username
        })}


//        view.userphoto.setOnClickListener({




//        view.images_recycler.layoutManager = GridLayoutManager(view.context, 2)
//        mFirebaseHelper.database.child("images").child(mFirebaseHelper.currentUid()!!)
//            .addValueEventListener(ValueEventListenerAdapter {
//                val images = it.children.map { it.getValue(String::class.java)!! }
//                view.images_recycler.adapter = ImagesAdapter(images)
//            })


        toChangeOne.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_ProfileScreen_to_UserChangeScreen)
        }

        toChangeTwo.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_ProfileScreen_to_UserChangeScreen)
        }

        fabSettings.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_ProfileScreen_to_SettingsScreen)
        }

        return view
    }
}






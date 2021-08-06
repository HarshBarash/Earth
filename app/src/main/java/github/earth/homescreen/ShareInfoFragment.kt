package github.earth.homescreen

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
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
import kotlinx.android.synthetic.main.fragment_shareinfo.*

class ShareInfoFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    val level = arrayOf(R.string.easy, R.string.medium, R.string.hard)


    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(LOG_PROFILE_FRAGMENT, "onCreate called")

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mFirebaseHelper = FirebaseHelper(activity)

        mFirebaseHelper.currentUserReference().addValueEventListener(ValueEventListenerAdapter {
            mUser = it.asUser()!!
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shareinfo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = Bundle()

        val image = arguments?.getString("ImageUri")

        val level = arrayOf(R.string.easy, R.string.medium, R.string.hard)


//        int n = Integer.parseInt(x.getText().toString());


//        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_dropdown_item_1line, level)
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//        spinnerLevel.setAdapter(arrayAdapter)
//        spinnerLevel.setAdapter(arrayAdapter)
//        spinnerLevel.setOnItemSelectedListener(this)

        nextbtntwo.setOnClickListener({
            if (!TextUtils.isEmpty(etTitle.getText()) &&
                !TextUtils.isEmpty(etMaterials.getText()) &&
                !TextUtils.isEmpty(etTime.getText())) {
                bundle.putString(
                    "Title", etTitle.toString())
                bundle.putString(
                    "Materials", etMaterials.toString())
                bundle.putString(
                    "Time", etTime.toString()
                    )
                bundle.putString(
                    "ImageUri", image
                )
                findNavController().navigate(R.id.action_shareInfoFragment_to_shareLinkFragment, bundle)
            } else {
                requireActivity().showToast("Please enter data")
            }
        })


//    spinnerLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//        override fun onItemSelected(
//            parent: AdapterView<*>?,
//            view: View?,
//            position: Int,
//            id: Long
//        ) {
//            parent?.getItemAtPosition(position) as
//        }
//
//        override fun onNothingSelected(parent: AdapterView<*>?) {
//
//        }
//    }
    }
}


package github.earth.settingsscreen

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import github.earth.R
import github.earth.authscreen.LoginActivity
import github.earth.authscreen.ValueEventListenerAdapter
import github.earth.models.User
import github.earth.utils.LOG_SETTINGS_FRAGMENT
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*


class SettingsFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var mUser: User


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        Log.d(LOG_SETTINGS_FRAGMENT, "onCreate")

        mAuth = FirebaseAuth.getInstance()

        view.update.setOnClickListener { updateProfile() }

        view.log_out_button.setOnClickListener{
                mAuth.signOut()
            }

        //вероятность низкая, но пусть будет
        mAuth.addAuthStateListener {
            if (it.currentUser == null) {
                val intent_toLogin = Intent (getActivity(), LoginActivity::class.java)
                getActivity()?.startActivity(intent_toLogin)
            }
        }


        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("users").child(mAuth.currentUser!!.uid)
            .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                mUser = it.getValue(User::class.java)!!
                etMail.setText(mUser.email, TextView.BufferType.EDITABLE)
                etUsername.setText(mUser.username, TextView.BufferType.EDITABLE)
        })

            return view
    }

    private fun updateProfile() {


        val user = User(
            email = etMail.text.toString(),
            username = etUsername.text.toString()
        )

        val error = validate(user)
        if (error == null) {
            if (user.email == mUser.email) {
                updateUser(user)
            } else {

            }
        } else {
            Toast.makeText(requireView().context , "Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUser(user: User) {
        val updatesMap = mutableMapOf<String, Any>()
        if (user.email != mUser.email) updatesMap["email"] = user.email
        if (user.username != mUser.username) updatesMap["username"] = user.username
        mDatabase.child("users").child(mAuth.currentUser!!.uid).updateChildren(updatesMap)
            .addOnCompleteListener{
                if (it.isSuccessful) {
                    Toast.makeText(requireView().context , "Profile saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireView().context, it.exception!!.message,  Toast.LENGTH_SHORT).show()

                }
            }
    }


    private fun validate(user: User): String? =
        when {
            user.username.isEmpty() -> "Please enter username"
            user.email.isEmpty() -> "Please enter email"
            else -> null
        }
}

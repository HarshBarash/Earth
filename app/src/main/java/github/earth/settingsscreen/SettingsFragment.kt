package github.earth.settingsscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import github.earth.R
import github.earth.authscreen.LoginActivity
import github.earth.authscreen.ValueEventListenerAdapter
import github.earth.models.User
import github.earth.utils.LOG_SETTINGS_FRAGMENT
import github.earth.views.FirebaseHelper
import github.earth.views.PasswordDialog
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*


class SettingsFragment : Fragment() {

    private lateinit var mUser: User
    private lateinit var mPendingUser: User
    private lateinit var mFirebase: FirebaseHelper
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        Log.d(LOG_SETTINGS_FRAGMENT, "onCreate")

        mAuth = FirebaseAuth.getInstance()

        view.update.setOnClickListener { updateProfile() }

        view.log_out_button.setOnClickListener{
                mAuth.signOut()
                val intent_toLogin = Intent (getActivity(), LoginActivity::class.java)
                getActivity()?.startActivity(intent_toLogin)
            }

        mFirebase = FirebaseHelper(activity)

        //вероятность низкая, но пусть будет
        mAuth.addAuthStateListener {
            if (it.currentUser == null) {
                val intent_toLogintwo = Intent (getActivity(), LoginActivity::class.java)
                getActivity()?.startActivity(intent_toLogintwo)
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
        mPendingUser = readInputs()
        val error = validate(mPendingUser)
        if (error == null) {
            if (mPendingUser.email == mUser.email) {
                updateUser(mPendingUser)
            } else {
                getActivity()?.let { PasswordDialog().show(it.getSupportFragmentManager() , "password_dialog") }
            }
        } else {
            Toast.makeText(requireView().context , error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun readInputs(): User {
        return User(
            username = etUsername.text.toString(),
            email = etMail.text.toString(),

        )
    }


    fun onPasswordConfirm(password: String) {
        if (password.isNotEmpty()) {
            val credential = EmailAuthProvider.getCredential(mUser.email, password)
            mFirebase.reauthenticate(credential) {
                mFirebase.updateEmail(mPendingUser.email) {
                    updateUser(mPendingUser)
                }
            }
        } else {
            Toast.makeText(requireView().context , "You should enter your password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUser(user: User) {
        val updatesMap = mutableMapOf<String, Any>()
        if (user.email != mUser.email) updatesMap["email"] = user.email
        if (user.username != mUser.username) updatesMap["username"] = user.username

        mFirebase.updateUser(updatesMap) {
            Toast.makeText(requireView().context , "Profile saved", Toast.LENGTH_SHORT).show()
        }
    }


    private fun validate(user: User): String? =
        when {
            user.username.isEmpty() -> "Please enter username"
            user.email.isEmpty() -> "Please enter email"
            else -> null
        }
}

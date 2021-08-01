package github.earth.settingsscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import github.earth.MainActivity
import github.earth.R
import github.earth.authscreen.LoginActivity
import github.earth.models.User
import github.earth.utils.*
import github.earth.views.PasswordDialog
import java.util.*

//TODO убрать лишние методы от Firease (Антон)
class SettingsFragment : Fragment(), PasswordDialog.Listener, View.OnClickListener {

    private lateinit var mUser: User
    private lateinit var mPendingUser: User
    private lateinit var mFirebase: FirebaseHelper
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    private val itemLanguages = arrayOf("English", "Russian", "German")

    private lateinit var spinLanguages: Spinner
    private lateinit var btnLogOut: Button
    private lateinit var etMail: EditText
    private lateinit var etUsername: EditText

    private lateinit var fltSave: FloatingActionButton

    private var sLanguage: String? = null
    private var newLanguage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(LOG_SETTINGS_FRAGMENT, "onCreate called")

        mAuth = FirebaseAuth.getInstance()
        mFirebase = FirebaseHelper(activity)
        mDatabase = FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        Log.d(LOG_SETTINGS_FRAGMENT, "onCreateView called")

        spinLanguages = view.findViewById(R.id.spinLanguages)
        btnLogOut = view.findViewById(R.id.btnLogOut)
        fltSave = view.findViewById(R.id.fltSave)
        etMail = view.findViewById(R.id.etMail)
        etUsername = view.findViewById(R.id.etUsername)

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, itemLanguages)
        spinLanguages.adapter = adapter

        listenSetter()
        getUserData()

        return view
    }

    override fun onStart() {
        super.onStart()
        Log.v(LOG_SETTINGS_FRAGMENT, "onStart called")

    }

    private fun getUserData() {

        //вероятность низкая, но пусть будет
        mAuth.addAuthStateListener {
            if (it.currentUser == null) {
                val intent_toLogintwo = Intent (activity, LoginActivity::class.java)
                activity?.startActivity(intent_toLogintwo)
            }
        }

        mDatabase.child("users").child(mAuth.currentUser!!.uid)
            .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                mUser = it.getValue(User::class.java)!!
                etMail.setText(mUser.email, TextView.BufferType.EDITABLE)
                etUsername.setText(mUser.username, TextView.BufferType.EDITABLE)
            })

        val spConfig = activity?.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE) ?: return
        sLanguage = spConfig.getString(SETTINGS_LANGUAGE, Locale.getDefault().displayLanguage.toString())

        if (sLanguage == ENGLISH)
            spinLanguages.setSelection(0)
        else if (sLanguage == RUSSIAN)
            spinLanguages.setSelection(1)
        else if (sLanguage == GERMAN)
            spinLanguages.setSelection(2)

    }

    private fun updateProfile() {
        mPendingUser = readInputs()
        val error = validate(mPendingUser)
        if (error == null) {
            if (mPendingUser.email == mUser.email) {
                updateUser(mPendingUser)
            } else {
                activity?.let { PasswordDialog().show(it.supportFragmentManager, "password_dialog") }
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

    override fun onPasswordConfirm(password: String) {
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

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnLogOut -> {
                mAuth.signOut()
                val intent_toLogin = Intent (activity, LoginActivity::class.java)
                activity?.startActivity(intent_toLogin)
            }
            R.id.fltSave -> {
                saveUserSettings()
            }
        }
    }

    private fun saveUserSettings() {

        val spConfig = activity?.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE) ?: return
        with(spConfig.edit()) {

            //if (newTheme != null)
            //    putString(SETTINGS_THEME, newTheme)

            //if (newAppIcon != null)
            //    putString(SETTINGS_APP_ICON, newAppIcon)
            Log.v(LOG_SETTINGS_FRAGMENT, "New Language: $newLanguage")

            if (newLanguage != null)
                putString(SETTINGS_LANGUAGE, newLanguage)

            apply()
        }
        (activity as MainActivity?)?.finish()
        startActivity(Intent(requireContext(),MainActivity::class.java))

    }

    private fun listenSetter() {

        btnLogOut.setOnClickListener(this)
        fltSave.setOnClickListener(this)

        spinLanguages.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.v(LOG_SETTINGS_FRAGMENT, "Spinner: " + itemLanguages[position])

                val selectedLang =
                    when {
                        itemLanguages[position] == itemLanguages[0] -> ENGLISH
                        itemLanguages[position] == itemLanguages[1] -> RUSSIAN
                        itemLanguages[position] == itemLanguages[2] -> GERMAN
                        else -> Log.v(LOG_SETTINGS_FRAGMENT, "Selected Language is null!")
                    }

                Log.v(LOG_SETTINGS_FRAGMENT, "Selected Language: $selectedLang\n sLanguage: $sLanguage")

                if (selectedLang == sLanguage)
                    return
                else
                    newLanguage = selectedLang.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }
}
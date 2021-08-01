package github.earth.authscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import github.earth.MainActivity
import github.earth.R
import github.earth.models.User
import github.earth.utils.LOG_REGISTER_ACTIVITY
import github.earth.utils.showToast
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        Log.v(LOG_REGISTER_ACTIVITY, "onCreate called")

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        signupbtn.setOnClickListener{

            val email = editTextTextSignUpEmail.text.toString()
            val username = editTextTextSignUpUsername.text.toString()
            val password = editTextTextSignUpPassword.text.toString()


            if (email.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                    mAuth.createUserWithEmailAndPassword(email, password) {
                        it.user?.let { it1 ->
                            mDatabase.createUser(it1.uid, mkUser(username, email)) {
                                startHomeActivity()
                            }
                        }
                    }
            } else {
                showToast("Please enter email, username and password")
            }
        }
    }


    private fun unknownRegisterError(it: Task<*>) {
        Log.e(LOG_REGISTER_ACTIVITY, "failed to create user profile", it.exception)
        showToast("Something wrong happened. Please try again later")
    }

    private fun startHomeActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun mkUser(username: String, email: String): User {
        return User( username = username, email = email)
    }

    private fun mkUsername(username: String) =
        username.lowercase(Locale.getDefault()).replace(" ", ".")

    private fun FirebaseAuth.fetchSignInMethodsForEmail(email: String,
                                                        onSuccess: (List<String>) -> Unit) {
        fetchSignInMethodsForEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess(it.result?.signInMethods ?: emptyList<String>())
            } else {
                showToast(it.exception!!.message!!)
            }
        }
    }

    private fun DatabaseReference.createUser(uid: String, user: User, onSuccess: () -> Unit) {
        val reference = child("users").child(uid)
        reference.setValue(user).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                unknownRegisterError(it)
            }
        }
    }

    private fun FirebaseAuth.createUserWithEmailAndPassword(email: String, password: String,
                                                            onSuccess: (AuthResult) -> Unit) {
        createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result?.let { it1 -> onSuccess(it1) }
                } else {
                    unknownRegisterError(it)
                }
            }
    }
}


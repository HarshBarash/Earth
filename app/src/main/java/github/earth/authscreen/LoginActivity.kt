package github.earth.authscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import github.earth.MainActivity
import github.earth.R
import github.earth.utils.LOG_LOGIN_ACTIVITY
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.editTextTextPersoEmail


class LoginActivity : AppCompatActivity(),  View.OnClickListener {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d(LOG_LOGIN_ACTIVITY, "onCreate")


        loginbtn.setOnClickListener(this)
        btnSignUp.setOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.loginbtn -> {
                val email = editTextTextPersoEmail.text.toString()
                val password = editTextTextPersonPassword.text.toString()
                if (validate(email, password)) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                        if (it.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }
                } else {
                    showToast("Please enter email and password")
                }
            }
            R.id.btnSignUp -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
    }


    private fun validate(email: String, password: String) =
        email.isNotEmpty() && password.isNotEmpty()

}

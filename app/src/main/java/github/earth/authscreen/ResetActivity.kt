package github.earth.authscreen

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import github.earth.R
import kotlinx.android.synthetic.main.activity_reset.*

class ResetActivity  : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)

        mAuth = FirebaseAuth.getInstance()

        resetbtn.setOnClickListener {
            val email = editTextTextResetEmail.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Enter your email!", Toast.LENGTH_SHORT).show()
            } else {
                mAuth!!.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Check email to reset your password!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        btnback.setOnClickListener {
            finish()
        }
    }
}
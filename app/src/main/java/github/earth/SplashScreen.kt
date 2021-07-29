package github.earth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import github.earth.authscreen.LoginActivity
import github.earth.utils.LOG_LOGIN_ACTIVITY
import github.earth.utils.LOG_SPLASH_SCREEN

class SplashScreen : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Log.v(LOG_SPLASH_SCREEN, "onCreate called")

        mAuth = FirebaseAuth.getInstance()

        Log.v(LOG_LOGIN_ACTIVITY, "Current user: ${mAuth.currentUser}")

        if (mAuth.currentUser != null)
            startMainActivity()
        else
            startLoginActivity()

    }

    private fun startMainActivity() {
        Log.v(LOG_SPLASH_SCREEN, "Starting MainActivity")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun startLoginActivity() {
        Log.v(LOG_SPLASH_SCREEN, "Starting LoginActivity")
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
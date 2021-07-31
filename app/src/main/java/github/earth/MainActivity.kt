package github.earth

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import github.earth.utils.LOG_MAIN_ACTIVITY
import github.earth.utils.SETTINGS_FILE
import github.earth.utils.SETTINGS_LANGUAGE
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sharephoto.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage()
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        val navigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        navigationView.setupWithNavController(navController)


    }

    override fun onStart() {
        super.onStart()
        Log.v(LOG_MAIN_ACTIVITY, "onStart called")

    }

    override fun onRestart() {
        super.onRestart()
        Log.v(LOG_MAIN_ACTIVITY, "onRestart called")

    }

    override fun onResume() {
        super.onResume()
        Log.v(LOG_MAIN_ACTIVITY, "onResume called")

    }

    override fun onPause() {
        super.onPause()
        Log.v(LOG_MAIN_ACTIVITY, "onPause called")

    }

    override fun onStop() {
        super.onStop()
        Log.v(LOG_MAIN_ACTIVITY, "onStop called")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(LOG_MAIN_ACTIVITY, "onDestroy called")

    }

    private fun setLanguage() {
        //Я крутой, мне похер на deprecated
        val sp = getSharedPreferences(SETTINGS_FILE, 0)
        val defLang = Locale.getDefault().displayLanguage

        val language: String = sp.getString(SETTINGS_LANGUAGE, null) ?: return

        Log.v(LOG_MAIN_ACTIVITY, "SharedPrefs lang: $language")

        if (defLang == language)
            return

        val locale = Locale(language)
        Locale.setDefault(locale)
        // Create a new configuration object
        val config = Configuration()
        // Set the locale of the new configuration
        config.locale = locale
        // Update the configuration of the Accplication context
        resources.updateConfiguration(
            config,
            resources.displayMetrics
        )
    }

}
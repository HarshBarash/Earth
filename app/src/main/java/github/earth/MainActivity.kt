package github.earth

import android.content.ComponentName
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import github.earth.utils.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setCustomTheme()
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

    fun changeIcon(iconName: String) {
        val packageManager = packageManager

        val sp = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE)
        val currentIcon = sp.getString(SETTINGS_APP_ICON, IC_DEFAULT_LAUNCHER)

        Log.v(LOG_MAIN_ACTIVITY, "New Icon Name: $iconName\nOld Icon Name: $currentIcon")

        val currentIconPackage = when(currentIcon) {
            IC_DEFAULT -> IC_DEFAULT_LAUNCHER
            IC_PURPLE -> IC_PURPLE_LAUNCHER
            IC_BEIGE -> IC_BEIGE_LAUNCHER
            IC_GRAY -> IC_GRAY_LAUNCHER
            IC_PINK -> IC_PINK_LAUNCHER
            IC_LIGHT_PINK -> IC_LIGHT_PINK_LAUNCHER
            IC_RED -> IC_RED_LAUNCHER
            IC_YELLOW -> IC_YELLOW_LAUNCHER
            IC_ORANGE -> IC_ORANGE_LAUNCHER
            IC_GREEN -> IC_GREEN_LAUNCHER
            IC_BLUE -> IC_BLUE_LAUNCHER
            else -> {
                Toast.makeText(this, "Error! Operation cancelled", Toast.LENGTH_SHORT)
                return
            }
        }


        disableIcon(currentIconPackage, packageManager)
        //Сохрание новой иконки
        sp.edit().putString(SETTINGS_APP_ICON, iconName).apply()

        when (iconName) {
            IC_DEFAULT      -> enableIcon(IC_DEFAULT_LAUNCHER, packageManager)
            IC_PURPLE       -> enableIcon(IC_PURPLE_LAUNCHER, packageManager)
            IC_BEIGE        -> enableIcon(IC_BEIGE_LAUNCHER, packageManager)
            IC_GRAY         -> enableIcon(IC_GRAY_LAUNCHER, packageManager)
            IC_PINK         -> enableIcon(IC_PINK_LAUNCHER, packageManager)
            IC_LIGHT_PINK   -> enableIcon(IC_LIGHT_PINK_LAUNCHER, packageManager)
            IC_RED          -> enableIcon(IC_RED_LAUNCHER, packageManager)
            IC_YELLOW       -> enableIcon(IC_YELLOW_LAUNCHER, packageManager)
            IC_ORANGE       -> enableIcon(IC_ORANGE_LAUNCHER, packageManager)
            IC_GREEN        -> enableIcon(IC_GREEN_LAUNCHER, packageManager)
            IC_BLUE         -> enableIcon(IC_BLUE_LAUNCHER, packageManager)
        }
    }

        private fun disableIcon(componentPackage: String, packageManager: PackageManager) {
            packageManager.setComponentEnabledSetting(
                ComponentName(PACKAGE, componentPackage),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }

        private fun enableIcon(componentPackage: String, packageManager: PackageManager) {
            packageManager.setComponentEnabledSetting(
                ComponentName(PACKAGE, componentPackage),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        }

        private fun setCustomTheme() {
            val sp = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE)
            val currentTheme = sp.getString(SETTINGS_THEME, THEME_DEFAULT)

            if (currentTheme != THEME_DEFAULT) {
                when (currentTheme) {
                    THEME_GREEN         -> { setTheme(R.style.Theme_Earth_Green)        }
                    THEME_PURPLE        -> { setTheme(R.style.Theme_Earth_Purple)       }
                    THEME_BEIGE         -> { setTheme(R.style.Theme_Earth_Beige)        }
                    THEME_ORANGE        -> { setTheme(R.style.Theme_Earth_Orange)       }
                    THEME_YELLOW        -> { setTheme(R.style.Theme_Earth_Yellow)       }
                    THEME_RED           -> { setTheme(R.style.Theme_Earth_Red)          }
                    THEME_GRAY          -> { setTheme(R.style.Theme_Earth_Gray)         }
                    THEME_PINK          -> { setTheme(R.style.Theme_Earth_Pink)         }
                    THEME_LIGHT_PINK    -> { setTheme(R.style.Theme_Earth_LightPink)    }
                    THEME_BLUE          -> { setTheme(R.style.Theme_Earth_Blue)         }
                    THEME_LIGHT_BLUE    -> { setTheme(R.style.Theme_Earth_LightBlue)    }
                }
            }
            else {
                return
            }

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

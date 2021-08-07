package github.earth

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import github.earth.homescreen.HomeViewModel
import github.earth.homescreen.HomeViewModelProviderFactory
import github.earth.services.ReminderService
import github.earth.settingsscreen.SettingsFragment
import github.earth.utils.*
import github.earth.widgets.UniversalAppWidget
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var homeViewModel: HomeViewModel
    private lateinit var navController: NavController

    lateinit var auth: FirebaseAuth
    lateinit var storageRef: StorageReference
    lateinit var firestoreRef: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        setCustomTheme()
        super.onCreate(savedInstanceState)
        setLanguage()
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        storageRef = Firebase.storage.reference
        firestoreRef = Firebase.firestore
        firestoreRef.clearPersistence()

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        val navigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        navigationView.setupWithNavController(navController)

        val tutorialRepository = TutorialRepository( storageRef, firestoreRef)

        //home
        val homeViewModelProviderFactory = HomeViewModelProviderFactory(tutorialRepository)
        homeViewModel =
            ViewModelProvider(this, homeViewModelProviderFactory).get(HomeViewModel::class.java)

        //исправить под аву
        val menu = navigationView.menu
        val menuItem = menu.findItem(R.id.profile)
        Glide.with(this)
            .asBitmap()
            .load("https://lh3.googleusercontent.com/proxy/tO3kS72ChposXy4SE6hETSZpnnQf2F51f0MFnRPxRg4nDzraN2Mhtpt39gTcR6hVe132dYsi-uhqP-jhyLrDQ7sVa-pzTRu0Wd_-e7vR")
            .apply(
                RequestOptions
                    .circleCropTransform()
                    .placeholder(R.drawable.ic_userphoto))
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    menuItem?.icon = BitmapDrawable(resources, resource)
                }
            })

        //ReminderService.startService(this, "Message")
        updateService()
        updateWidgets()
    }

    fun updateWidgets() {
        val intent = Intent(this, UniversalAppWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager
            .getInstance(application)
            .getAppWidgetIds(ComponentName(applicationContext,UniversalAppWidget::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)
    }

    fun updateService() {

        val needTime = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE).getString(
            SETTINGS_REMIND_TIME, DEFAULT_RMD_TIME)

        Log.i(LOG_MAIN_ACTIVITY,"Service run: ${ReminderService.isRunning()}")

        if (needTime != null) {
            if (ReminderService.isRunning()) {
                ReminderService.stopService(this)
                ReminderService.startService(this, needTime)
            } else
                ReminderService.startService(this, needTime)

        }
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
        val currentIcon = sp.getString(SETTINGS_APP_ICON, IC_DEFAULT)

        Log.v(LOG_MAIN_ACTIVITY, "New Icon Name: $iconName\nOld Icon Name: $currentIcon")

        val currentIconPackage = when(currentIcon) {
            IC_DEFAULT      -> IC_DEFAULT_LAUNCHER
            IC_PURPLE       -> IC_PURPLE_LAUNCHER
            IC_BEIGE        -> IC_BEIGE_LAUNCHER
            IC_GRAY         -> IC_GRAY_LAUNCHER
            IC_PINK         -> IC_PINK_LAUNCHER
            IC_LIGHT_PINK   -> IC_LIGHT_PINK_LAUNCHER
            IC_RED          -> IC_RED_LAUNCHER
            IC_YELLOW       -> IC_YELLOW_LAUNCHER
            IC_ORANGE       -> IC_ORANGE_LAUNCHER
            IC_GREEN        -> IC_GREEN_LAUNCHER
            IC_BLUE         -> IC_BLUE_LAUNCHER

            else -> {
                Toast.makeText(this, "Error! Operation cancelled", Toast.LENGTH_SHORT)
                return
            }
        }


        if (iconName == currentIcon) {
            Toast.makeText(this, R.string.alreadySelectedIcon, Toast.LENGTH_SHORT).show()
            return
        } else {
            Toast.makeText(this, R.string.choice, Toast.LENGTH_SHORT).show()
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

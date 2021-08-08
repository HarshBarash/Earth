package github.earth

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import github.earth.utils.*

class ErrorActivity : AppCompatActivity() {

    private lateinit var btnAction: Button
    private lateinit var ivEarth: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        setCustomTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        val config = CustomActivityOnCrash.getConfigFromIntent(intent)

        /**
         * If config is null or not getting an intent simply finish the app
         */
        if (config == null) {
            finish()
            return
        }

        btnAction = findViewById(R.id.btnAction)
        ivEarth = findViewById(R.id.ivEarth)

        val rotation = AnimationUtils.loadAnimation(
                this, R.anim.earth_rotation)

        ivEarth.startAnimation(rotation)

        if (config.isShowRestartButton && config.restartActivityClass != null) {
            btnAction.text = "Restart App"
            btnAction.setOnClickListener {
                CustomActivityOnCrash.restartApplication(
                    this,
                    config
                )
            }
        } else {
            btnAction.text = "Close App"
            btnAction.setOnClickListener {
                CustomActivityOnCrash.closeApplication(
                    this,
                    config
                )
            }
        }

    }

    private fun setCustomTheme() {
        val sp = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE)
        val currentTheme = sp.getString(SETTINGS_THEME, THEME_DEFAULT)
        val currentThemeAction = sp.getString(SETTINGS_THEME_ACTION, ACTION_SYSTEM)

        when (currentThemeAction) {
            ACTION_SYSTEM -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                } else {
                    Log.v(LOG_MAIN_ACTIVITY, "System doesn't support action FOLLOW_SYSTEM. Ignoring param and continue.")
                }
            }
            ACTION_DAY -> { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
            ACTION_NIGHT -> { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
        }

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

}
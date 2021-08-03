package github.earth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import cat.ereza.customactivityoncrash.CustomActivityOnCrash

class ErrorActivity : AppCompatActivity() {

    private lateinit var btnAction: Button
    private lateinit var ivEarth: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
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
}
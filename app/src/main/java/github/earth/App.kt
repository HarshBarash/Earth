package github.earth

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import cat.ereza.customactivityoncrash.config.CaocConfig
import github.earth.utils.APP_NAME
import github.earth.utils.NTF_REMINDER_CHANNEL

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        setErrorScreen()
        //createNotificationsChannel()
    }

    private fun setErrorScreen() {
        CaocConfig.Builder.create()
            .trackActivities(true)
            .errorActivity(ErrorActivity::class.java)
            .apply()

    }

}
package github.earth

import android.app.Application
import cat.ereza.customactivityoncrash.config.CaocConfig

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        //Переопределение Error Screen
        CaocConfig.Builder.create()
            .trackActivities(true)
            .errorActivity(ErrorActivity::class.java)
            .apply()
    }
}
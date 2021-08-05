package github.earth.services

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PRIVATE
import androidx.core.app.NotificationCompat.VISIBILITY_SECRET
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import github.earth.MainActivity
import github.earth.R
import github.earth.utils.APP_NAME
import github.earth.utils.LOG_REMINDER_SERVICE
import github.earth.utils.NTF_FRG_SERV_CHANNEL
import github.earth.utils.NTF_REMINDER_CHANNEL
import kotlinx.coroutines.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ReminderService : Service() {

    companion object {

        const val PAUSE_TIME = 60000L
        const val NEED_TIME = "20:12" //Пока константа, потом поменяем

        fun startService(context: Context, message: String) {
            val serviceIntent = Intent(context, ReminderService::class.java)
            serviceIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, serviceIntent)
            //context.startService(serviceIntent)
        }
        fun stopService(context: Context) {
            val serviceIntent = Intent(context, ReminderService::class.java)
            context.stopService(serviceIntent)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.i(LOG_REMINDER_SERVICE, "Service onBind")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(LOG_REMINDER_SERVICE, "Service onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(LOG_REMINDER_SERVICE, "Service onStartCommand")

        createNotificationsChannel()

        val input = intent?.getStringExtra("inputExtra")
        var cont = false

        CoroutineScope(Dispatchers.Default).launch {

            while (!cont) {
                val currentDate = Date()
                val timeFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault()) //"HH:mm:ss"
                val currentTime : String = timeFormat.format(currentDate)

                Log.i(LOG_REMINDER_SERVICE, "Need Time: $NEED_TIME\nCurrent Time: $currentTime")

                if (currentTime == NEED_TIME) {
                    sendNotification()
                } else {
                    Log.i(LOG_REMINDER_SERVICE, "Time does not match, starting pause...")
                }
                delay(PAUSE_TIME)
            }
            //startForeground(1, ntf)
        }

        val ntfFrgServ = NotificationCompat.Builder(applicationContext, NTF_FRG_SERV_CHANNEL)
            .setContentTitle("We will remind you about sorting in $NEED_TIME"/*R.string.contentTittle.toString()*/)
            //.setContentText("Жду тебя на помойке"/*R.string.contentText.toString()*/) // или input
            .setSmallIcon(R.drawable.ic_ntf_earth)
            //.setLargeIcon()
            //.setContentIntent(pendingIntent)
            .setSilent(true)
            .setAutoCancel(true)
            .setShowWhen(false)
            .setVisibility(VISIBILITY_PRIVATE)
            .build()


        startForeground(1, ntfFrgServ)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(LOG_REMINDER_SERVICE, "Service onDestroy")

    }

    private fun createNotificationsChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Объявление канала уведомлений
            val ntfReminder = NotificationChannel(
                NTF_REMINDER_CHANNEL,
                "Remind",
                NotificationManager.IMPORTANCE_DEFAULT)

            val ntfFrgServ = NotificationChannel(
                NTF_FRG_SERV_CHANNEL,
                "Service",
                NotificationManager.IMPORTANCE_MIN)

            //Кастомизация
            ntfReminder.description = "Reminder channel"
            ntfFrgServ.description = "Foreground service channel"
            ntfFrgServ.lockscreenVisibility = VISIBILITY_SECRET

            //Создание канала уведомлений
            val manager: NotificationManager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(ntfReminder)
            manager.createNotificationChannel(ntfFrgServ)
        }

    }

    private fun sendNotification() {
        val ntfIntent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext,0,ntfIntent,0)

        val ntf = NotificationCompat.Builder(applicationContext, NTF_REMINDER_CHANNEL)
            .setContentTitle("Пора сортировать мусор, ублюдок"/*R.string.contentTittle.toString()*/)
            .setContentText("Жду тебя на помойке"/*R.string.contentText.toString()*/) // или input
            .setSmallIcon(R.drawable.ic_ntf_earth)
            //.setLargeIcon()
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)){
            notify(2, ntf.build())
        }

    }

}

/* Краткая инструкция
 *
 * START_STICKY - сервис будет перезапущен после того, как был убит системой
 * START_NOT_STICKY - сервис не будет перезапущен после того, как был убит системой
 * START_REDELIVER_INTENT - сервис будет перезапущен после того, как был убит системой.
 *                          Кроме того,сервис снова получит все вызовы startService, которые
 *                          не были завершены методом stopSelf()
 *
 * stopSelf() - остановит сама себя
 * stopService() - остановка из любого компонента. В метод передаётся intent
 *
 * https://stackoverflow.com/questions/10962418/how-to-startforeground-without-showing-notification
 * Мб когда то пригодится
 */


package github.earth.utils

import android.Manifest
import android.Manifest.permission.CAMERA
import android.Manifest.permission_group.CAMERA
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import github.earth.MainActivity
import java.lang.reflect.ReflectPermission


/* Файл для работы с разрешениями*/

const val CAMERA = Manifest.permission.CAMERA
const val PERMISSION_REQUEST = 200

fun checkPermission(permission: String): Boolean {


    /* Функция принимает разрешение и проверяет, если разрешение еще не было
    * предоставлено запускает окно с запросом пользователю */
    return if (Build.VERSION.SDK_INT >= 23
        && ContextCompat.checkSelfPermission(
            APP_ACTIVITY,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(APP_ACTIVITY, arrayOf(permission), PERMISSION_REQUEST)
        false
    } else true
}
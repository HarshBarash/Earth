package github.earth.utils

import android.Manifest
import android.content.Context

object GalleryUtility {

    fun hasStoragePermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
}
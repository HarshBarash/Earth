package github.earth.placesscreen.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import github.earth.placesscreen.PlacesFragment

class map_info_dialog: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Если да, вам придется добавить фото с этой точки")
                .setTitle("Вы уверены что здесь есть мусор?")
                .setPositiveButton("Да"
                ) { dialog, id ->

                    dialog.cancel()
                    dialog.dismiss()
                }
                .setNegativeButton("Нет"
                ) { dialog, id ->
                    dialog.cancel()
                    dialog.dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
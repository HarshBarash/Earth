package github.earth.placesscreen.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import github.earth.room.places_room.PlacesViewModel

class map_edit_dialog: DialogFragment() {
    private lateinit var mPlacesViewModel: PlacesViewModel
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle("Здесь прибрано?")
                .setPositiveButton("Да"
                ) { dialog, id ->
                    //markerEdit()
                    dialog.cancel()
                    dialog.dismiss()
                }
                .setNegativeButton("Нет"
                ) { dialog, id ->
                    dialog.cancel()
                    dialog.dismiss()
                }
                .setNeutralButton("Удалить"
                ) { dialog, id ->
                    //marker.remove()
                    dialog.cancel()
                    dialog.dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
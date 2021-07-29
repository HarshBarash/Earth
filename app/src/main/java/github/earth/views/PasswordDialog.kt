package github.earth.views

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import github.earth.R
import kotlinx.android.synthetic.main.dialog_password.view.*

class PasswordDialog : DialogFragment(){

    private lateinit var mListener: Listener

    interface Listener {
        fun onPasswordConfirm(password: String)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as Listener
        } catch (e: ClassCastException) {

        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_password, null)
        return AlertDialog.Builder(requireContext()).setView(view)
            .setPositiveButton(android.R.string.ok, {_, _ ->
                mListener.onPasswordConfirm(view.password_input.text.toString())
            })
            .setNegativeButton(android.R.string.cancel, {_, _ ->
                // do nothing
            })
            .setTitle(R.string.please_enter_password)
            .create()
    }
}
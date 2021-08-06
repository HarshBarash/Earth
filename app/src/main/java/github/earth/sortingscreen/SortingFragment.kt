package github.earth.sortingscreen

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import github.earth.MainActivity
import github.earth.R
import github.earth.utils.*
import kotlinx.android.synthetic.main.fragment_sorting.view.*

class SortingFragment : Fragment(), View.OnClickListener{

    private lateinit var fltNtf : FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.v(LOG_SORTING_FRAGMENT, "onCreateView called")
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_sorting, container, false)

        //Навигация дальше
//        view.findViewById<Button>(R.id.signup_btn).setOnClickListener {
//            findNavController().navigate(R.id.action_register_to_registered)
//        }

        fltNtf = rootView.findViewById(R.id.fltNtf)
        fltNtf.setOnClickListener(this)

        return rootView
    }

    override fun onStart() {
        super.onStart()

    }

    private fun pickTime() {

        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                //.setTitle("Select Appointment time")
                .build()

        picker.addOnPositiveButtonClickListener {
            Log.v(LOG_SORTING_FRAGMENT, "onPositiveButton callback")
            // call back code

            var pickMin = "00"
            if (picker.minute < 10) {
                when (picker.minute) {
                    1 -> pickMin = "01"
                    2 -> pickMin = "02"
                    3 -> pickMin = "03"
                    4 -> pickMin = "04"
                    5 -> pickMin = "05"
                    6 -> pickMin = "06"
                    7 -> pickMin = "07"
                    8 -> pickMin = "08"
                    9 -> pickMin = "09"
                }
            } else {
                pickMin = "${picker.minute}"
            }

            val time = "${picker.hour}:${pickMin}"
            Log.v(LOG_SORTING_FRAGMENT, "Returned time: $time")
            updateRemindTime(time)
            (activity as MainActivity).updateService()
            (activity as MainActivity).updateWidgets()
        }
        picker.addOnNegativeButtonClickListener {
            // call back code
            Log.v(LOG_SORTING_FRAGMENT, "onNegativeButton callback")
        }
        picker.addOnCancelListener {
            // call back code
            Log.v(LOG_SORTING_FRAGMENT, "onCancel callback")
        }
        picker.addOnDismissListener {
            // call back code
            Log.v(LOG_SORTING_FRAGMENT, "onDismiss callback")
        }

        picker.show((activity as MainActivity).supportFragmentManager, "tag");
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fltNtf -> {
                pickTime()
            }
        }
    }

    private fun updateRemindTime(time: String) {
        val sp = (activity as MainActivity).getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE)  ?: return
        with(sp.edit()) {

            putString(SETTINGS_REMIND_TIME, time)

            apply()
        }

    }

}
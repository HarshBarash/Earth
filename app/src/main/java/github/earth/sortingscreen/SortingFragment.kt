package github.earth.sortingscreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import github.earth.MainActivity
import github.earth.R
import github.earth.utils.LOG_SORTING_FRAGMENT
import github.earth.utils.showToast
import kotlinx.android.synthetic.main.fragment_sorting.view.*

class SortingFragment : Fragment(), View.OnClickListener{

    private lateinit var fltNtf : FloatingActionButton

    @SuppressLint("WrongConstant")
    public fun checkandGetpermissions(){
        if(PermissionChecker.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED){
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 100)
        }
        else{
            requireActivity().showToast("Camera permission granted")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                requireActivity().showToast("Camera permission granted")
            }
            else{
                requireActivity().showToast("Permission Denied")
            }
        }
    }

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
                .setMinute(10)
                //.setTitle("Select Appointment time")
                .build()

        picker.addOnPositiveButtonClickListener {
            Log.v(LOG_SORTING_FRAGMENT, "onPositiveButton callback")
            // call back code
            val time = "${picker.hour}:${picker.minute}"
            Log.v(LOG_SORTING_FRAGMENT, "Returned time: $time")
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

    private fun updateRemindTime() {
        //val sp = (activity as MainActivity)
    }

}
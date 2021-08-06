package github.earth.sortingscreen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import github.earth.MainActivity
import github.earth.R
import github.earth.ml.ModelUnquant
import github.earth.utils.LOG_SETTINGS_FRAGMENT
import github.earth.utils.LOG_SORTING_FRAGMENT
import github.earth.utils.showToast
import kotlinx.android.synthetic.main.fragment_sorting.*
import kotlinx.android.synthetic.main.fragment_sorting.view.*
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class SortingFragment : Fragment(), View.OnClickListener {

    private lateinit var fltNtf: FloatingActionButton


    //ML
    lateinit var bitmap: Bitmap
    lateinit var appCtx: Application
    lateinit var btnMLrn : Button


    @SuppressLint("WrongConstant")
    public fun checkandGetpermissions() {
        if (PermissionChecker.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
        } else {
            requireActivity().showToast("Camera permission granted")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requireActivity().showToast("Camera permission granted")
            } else {

                requireActivity().showToast("Permission Denied")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(LOG_SORTING_FRAGMENT, "onCreate called")

        // handling permissions
        checkandGetpermissions()

        appCtx = requireActivity().application

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(LOG_SORTING_FRAGMENT, "onCreateView called")
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_sorting, container, false)

        btnMLrn = rootView.findViewById(R.id.btnMLrn) //здесь сюда срочно
            //махнуть на assets


        //Навигация дальше
//        view.findViewById<Button>(R.id.signup_btn).setOnClickListener {
//            findNavController().navigate(R.id.action_register_to_registered)
//        }

        fltNtf = rootView.findViewById(R.id.fltNtf)
        fltNtf.setOnClickListener(this)

        btnMLrn.setOnClickListener(View.OnClickListener {
//            Log.d("mssg", "button pressed")
//            var intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "image/*"


            var camera : Intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(camera, 200)

//            startActivityForResult(intent, 250)
        })

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

    //Для панели админа
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 250){

            img_view.setImageURI(data?.data)

            var uri : Uri?= data?.data
            bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
        }
        else if(requestCode == 200 && resultCode == Activity.RESULT_OK){
            bitmap = data?.extras?.get("data") as Bitmap
            img_view.setImageBitmap(bitmap)

            var resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            val model = ModelUnquant.newInstance(requireActivity())


            var tbuffer = TensorImage.fromBitmap(resized)
            var byteBuffer = tbuffer.buffer

// Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(byteBuffer)

// Runs model inference and gets result.
            val outputs = model?.process(inputFeature0)
            val outputFeature0 = outputs?.outputFeature0AsTensorBuffer

            var max = outputFeature0?.let { it1 -> getMax(it1.floatArray) }

            val labels = appCtx.assets.open("labels.txt").bufferedReader().use { it.readText() }.split("\n")

            sorttext.setText(labels[max!!])

// Releases model resources if no longer used.
            if (model != null) {
                model.close()
            }
        }}


    fun getMax(arr: FloatArray): Int {
        var ind = 0;
        var min = 0.0f;

        for (i in 0..10) {
            if (arr[i] > min) {
                min = arr[i]
                ind = i;
            }
        }
        return ind
    }
}
package github.earth.sortingscreen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.storage.StorageManager
import android.provider.Contacts.Intents.Insert.ACTION
import android.provider.ContactsContract.Intents.Insert.ACTION
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import github.earth.MainActivity
import github.earth.R

import github.earth.utils.LOG_SORTING_FRAGMENT
import github.earth.utils.SETTINGS_FILE
import github.earth.utils.SETTINGS_REMIND_TIME
import github.earth.utils.showToast
import kotlinx.android.synthetic.main.fragment_sorting.*
import kotlinx.android.synthetic.main.fragment_sorting.view.*
import java.lang.Exception


class SortingFragment : Fragment(), View.OnClickListener {

    lateinit var ivPicture: ImageView
    lateinit var tvResult: TextView
    lateinit var btnChoosePicture: Button
    lateinit var calc_btn_onSorting: ImageView
    private val CAMERA_PERMISSION_CODE = 123
    private val STORAGE_PERMISSION_CODE = 113


    private lateinit var fltNtf: FloatingActionButton

    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    lateinit var inputImage: InputImage

    lateinit var imageLabeler: ImageLabeler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(LOG_SORTING_FRAGMENT, "onCreate called")


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.v(LOG_SORTING_FRAGMENT, "onCreateView called")
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_sorting, container, false)

        //Навигация дальше
//        view.findViewById<Button>(R.id.signup_btn).setOnClickListener {
//            findNavController().navigate(R.id.action_register_to_registered)
//        }

        fltNtf = rootView.findViewById(R.id.fltNtf)
        fltNtf.setOnClickListener(this)

        ivPicture = rootView.findViewById(R.id.img_view)
        tvResult = rootView.findViewById(R.id.sorttext)
        btnChoosePicture = rootView.findViewById(R.id.btnMLrn)

        imageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        calc_btn_onSorting = rootView.findViewById(R.id.calc_icon)

        calc_btn_onSorting.setOnClickListener {
            val dialog = showCalcDialog(rootView)
            dialog.show()
        }

        btnChoosePicture.setOnClickListener {
            val options = arrayOf("Camera", "Gallery")
            val builder = AlertDialog.Builder(requireContext())

            //builder.setTitle("Pick a option")


            builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
                if (which == 0) {
                    val camera_permission =
                        ContextCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.CAMERA)
                    if (camera_permission == PackageManager.PERMISSION_GRANTED) {
                        val camerIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraLauncher.launch(camerIntent)
                    }else {
                        ActivityCompat.requestPermissions(requireActivity(),
                            arrayOf(Manifest.permission.CAMERA),
                            targetRequestCode)
                    }
                } else {
                    val storageIntent = Intent()
                    storageIntent.setType("image/*")
                    storageIntent.setAction(Intent.ACTION_GET_CONTENT)
                    galleryLauncher.launch(storageIntent)

                }})


            builder.show()

        }

        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult> {
                override fun onActivityResult(result: ActivityResult?) {
                    val data = result?.data
                    try {
                        val photo = data?.extras?.get("data") as Bitmap
                        ivPicture.setImageBitmap(photo)
                        inputImage = InputImage.fromBitmap(photo, 0)
                        processImage()

                    } catch (e: Exception) {
                        Log.d(LOG_SORTING_FRAGMENT, "onActivityResult: ${e.message}")
                    }
                }
            }
        )

        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult> {
                override fun onActivityResult(result: ActivityResult?) {
                    val data = result?.data
                    try {
                        inputImage = InputImage.fromFilePath(requireActivity(), data?.data)
                        ivPicture.setImageURI(data?.data)
                        processImage()
                    } catch (e: Exception) {

                    }
                }
            }
        )

        return rootView
    }

    @SuppressLint("SetTextI18n")
    fun showCalcDialog(view_: View): AlertDialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            var calc_et: EditText? = view_?.findViewById(R.id.calc_et)
            val layout = inflater.inflate(R.layout.custom_dialog_calculator, null)
            val calc_btn: Button? = view_?.findViewById(R.id.calc_btn)
            var result: TextView? = view_?.findViewById(R.id.result_calc)

            calc_btn?.setOnClickListener {
                result?.text = (calc_et?.text.toString().toInt().toDouble() / 60.0).toString() + " trees"
            }

            builder
                .setView(layout)
                .setNegativeButton("Отмена",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                        dialog.dismiss()
                    })

            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    //ML здесь
    private fun processImage() {
        imageLabeler.process(inputImage)
            .addOnSuccessListener {

                var result = ""

                for (label in it) {
                    val text = label.text
                    result = result + "\n" + label.text
                }

                tvResult.text = result
            }.addOnFailureListener {
                Log.d(LOG_SORTING_FRAGMENT, "processImage: ${it.message}")
            }
    }


    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(requireContext(),
                permission) == PackageManager.PERMISSION_DENIED
        ) {

            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
        } else {
            activity?.showToast("Permission Granted already")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                activity?.showToast("Camera Permission Granted")
            } else {
                activity?.showToast("Camera Permission Denied")

            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                activity?.showToast("Storage Permission Granted")
            } else {
                activity?.showToast("Storag Permission Denied")

            }
        }
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
            //val time = "${picker.hour}:${picker.minute}"

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
        val sp =
            (activity as MainActivity).getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE) ?: return
        with(sp.edit()) {

            putString(SETTINGS_REMIND_TIME, time)

            apply()
        }


    }

}
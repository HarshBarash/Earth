package github.earth.settingsscreen

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import github.earth.R
import github.earth.models.User
import github.earth.utils.FirebaseHelper
import github.earth.utils.ValueEventListenerAdapter
import github.earth.utils.showToast
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_userchange.*
import kotlinx.android.synthetic.main.fragment_userchange.view.*
import java.io.ByteArrayOutputStream

//ФРАГМЕНТ НА РЕКОНСТРУКЦИИ (8.08-12.08)
class UserChangeFragment : Fragment() {

    private val DEFAULT_IMAGE_URL = "https://picsum.photos/200"
    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var mUser : User


    private lateinit var imageUri: Uri
    private val REQUEST_IMAGE_CAPTURE = 100

    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_userchange, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mFirebaseHelper = FirebaseHelper(activity)

        currentUser?.let { user ->
            Glide.with(this)
                .load(user.photoUrl)
                .circleCrop() //todo de отпробовать, если качесво сильно хуже
                .into(image_view)
            text_email.text = user.email
            et_username_userchange.setText(user.displayName)


            if (user.isEmailVerified) {
                text_not_verified.visibility = View.INVISIBLE
            } else {
                text_not_verified.visibility = View.VISIBLE
            }
        }

        image_view.setOnClickListener {
            takePictureIntent()
        }

        button_save.setOnClickListener {

            val photo = when {
                ::imageUri.isInitialized -> imageUri
                currentUser?.photoUrl == null -> Uri.parse(DEFAULT_IMAGE_URL)
                else -> currentUser.photoUrl
            }

            val name = et_username_userchange.text.toString().trim()

            if (name.isEmpty()) {
                et_username_userchange.error = "name required"
                et_username_userchange.requestFocus()
                return@setOnClickListener
            }

            val updates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(photo)
                .build()

            progressbar.visibility = View.VISIBLE

            currentUser?.updateProfile(updates)
                ?.addOnCompleteListener { task ->
                    progressbar.visibility = View.INVISIBLE
                    if (task.isSuccessful) {
                        context?.showToast("Profile Updated")
                    } else {
                        context?.showToast(task.exception?.message!!)
                    }
                }

        }


        text_not_verified.setOnClickListener {

            currentUser?.sendEmailVerification()
                ?.addOnCompleteListener {
                    if(it.isSuccessful){
                        context?.showToast("Verification Email Sent")
                    }else{
                        context?.showToast(it.exception?.message!!)
                    }
                }

        }

//        text_phone.setOnClickListener {
//            val action = ProfileFragmentDirections.actionVerifyPhone()
//            Navigation.findNavController(it).navigate(action)
//        }

        text_email.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_UserChange_to_updateEmailFragment)

        }

        text_password.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_UserChange_to_updatePasswordFragment)
        }
    }

    private fun takePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
            pictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uploadImageAndSaveUri(imageBitmap)
        }
    }

    private fun uploadImageAndSaveUri(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        val upload = storageRef.putBytes(image)

        progressbar_pic.visibility = View.VISIBLE
        upload.addOnCompleteListener { uploadTask ->
            progressbar_pic.visibility = View.INVISIBLE

            if (uploadTask.isSuccessful) {
                storageRef.downloadUrl.addOnCompleteListener { urlTask ->
                    urlTask.result?.let {
                        imageUri = it
                        activity?.showToast(imageUri.toString())
                        image_view.setImageBitmap(bitmap)
                    }
                }
            } else {
                uploadTask.exception?.let {
                    activity?.showToast(it.message!!)
                }
            }
        }

    }

}

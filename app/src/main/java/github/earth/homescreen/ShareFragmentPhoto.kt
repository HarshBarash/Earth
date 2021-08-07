package github.earth.homescreen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import github.earth.MainActivity
import github.earth.R
import github.earth.models.User
import github.earth.utils.*
import kotlinx.android.synthetic.main.fragment_shareinfo.*
import kotlinx.android.synthetic.main.fragment_sharelink.*
import kotlinx.android.synthetic.main.fragment_sharephoto.*
import kotlinx.android.synthetic.main.fragment_sharephoto.view.*
import kotlinx.android.synthetic.main.fragment_single_tutorial.*


class ShareFragmentPhoto  : Fragment() {


    private lateinit var viewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).homeViewModel

        updatingPostImage()

//        btnCreatePost.setOnClickListener {
//            uploadTutorial()
//        }

        viewModel.uploadTutorialState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                    this.findNavController()
                        .navigate(R.id.action_SharePhotoScreen_to_HomeFragment)
                    viewModel.doneTutorialImageUri()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
            viewModel.doneTutorialState()
        })

        ivBackBtn.setOnClickListener {
            Toast.makeText(activity, "Post Creation Cancelled", Toast.LENGTH_SHORT).show()
            this.findNavController().navigateUp()
            viewModel.doneTutorialImageUri()
        }
    }

    private fun updatingPostImage() {
        ivTutorialImageShare.setOnClickListener {
            pickImageFromGallery()
        }

        viewModel.tutorialImageUri.observe(viewLifecycleOwner, Observer {
            it?.let {
                Glide.with(this).load(it).into(ivTutorialImageShare)
            }
        })
        //Отображение профиля при onCreate
//        viewModel.profileImageUri.observe(viewLifecycleOwner, Observer {
//            Glide.with(this).load(it).into(ivProfileImage)
//        })
    }

    private fun uploadTutorial() {
        val title = etTutorialTitle.text.toString()
        val description = etTutorialDescription.text.toString()
        viewModel.uploadTutorialDetailsToFirestore(title, description)
    }

    //вынести на финальный проект.
    private fun showProgressBar() {
        nextbuttonone.visibility = View.INVISIBLE
        createProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        nextbuttonone.visibility = View.VISIBLE
        createProgressBar.visibility = View.INVISIBLE
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, Constants.IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.IMAGE_PICK_CODE) {
            data?.data?.let {
                viewModel.setTutorialImageUri(it)
            }
        }
    }

}
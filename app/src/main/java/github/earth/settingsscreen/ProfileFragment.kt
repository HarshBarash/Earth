package github.earth.settingsscreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import github.earth.R
import github.earth.models.User
import github.earth.utils.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import github.earth.MainActivity
import kotlinx.android.synthetic.main.fragment_userchange.*



class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var viewModel: ProfileViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).profileViewModel

        setImageAndUsername()

//        viewModel.updateState.observe(viewLifecycleOwner, Observer {
//            when (it) {
//                is Resource.Success -> {
//                    hideProgressBar()
//                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
//                }
//                is Resource.Error -> {
//                    hideProgressBar()
//                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
//                }
//                is Resource.Loading -> {
//                    showProgressBar()
//                }
//            }
//            viewModel.doneUpdateState()
//        })

        ivProfileImage.setOnClickListener {
            pickImageFromGallery()
        }


            //todo закинем дальше
//        btnUpdate.setOnClickListener {
//            updateProfile()
//        }
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
                viewModel.setImageUri(it)
            }
        }
    }

    private fun setImageAndUsername() {
        viewModel.getCurrentUserDetails()
        viewModel.profileImageUri.observe(viewLifecycleOwner, Observer {
            Glide.with(this).load(it).placeholder(ivProfileImage.drawable).into(ivProfileImage)
        })
        viewModel.profileUsername.observe(viewLifecycleOwner, Observer {
            it?.let {
                username_text.setText(it)
            }
        })
    }

    //дальше
//    private fun updateProfile() {
//        val username = etProfileUsername.text.toString()
//        viewModel.updateProfile(username)
//    }

//    private fun showProgressBar() {
//        btnUpdate.visibility = View.INVISIBLE
//        updateProgressBar.visibility = View.VISIBLE
//    }
//
//    private fun hideProgressBar() {
//        btnUpdate.visibility = View.VISIBLE
//        updateProgressBar.visibility = View.INVISIBLE
//    }
}






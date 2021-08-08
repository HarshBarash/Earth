package github.earth.settingsscreen

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import github.earth.MainActivity
import github.earth.R
import github.earth.utils.Constants
import github.earth.utils.Resource
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var viewModel: ProfileViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).profileViewModel

        setImageAndUsername()


        viewModel.updateState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
            viewModel.doneUpdateState()
        })

        ivProfileImage.setOnClickListener {
            pickImageFromGallery()
        }

        btnLogout.setOnClickListener {
            confirmLogout()
        }

            //todo закинем дальше
        btnUpdate.setOnClickListener {
            updateProfile()
        }
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


    private fun confirmLogout() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to Logout")
            .setPositiveButton("Logout") { dialog, _ ->
                viewModel.userLogout()
                this.findNavController().navigate(R.id.action_ProfileFragment_to_home)
                dialog.cancel()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    //дальше
    private fun updateProfile() {
        val username = username_text.text.toString()
        viewModel.updateProfile(username)
    }

    private fun showProgressBar() {
        btnUpdate.visibility = View.INVISIBLE
        updateProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        btnUpdate.visibility = View.VISIBLE
        updateProgressBar.visibility = View.INVISIBLE
    }
}






package github.earth.homescreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import github.earth.MainActivity
import github.earth.R
import github.earth.utils.*
import kotlinx.android.synthetic.main.fragment_sharephoto.*


class ShareFragmentPhoto  : Fragment(R.layout.fragment_sharephoto) {

    private lateinit var viewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).homeViewModel

        updatingPostImage()

        publish.setOnClickListener {
            uploadTutorial()
        }

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

        //если надо картинку профиля
//        viewModel.profileImageUri.observe(viewLifecycleOwner, Observer {
//            Glide.with(this).load(it).into(ivProfileImage)
//        })
    }

    private fun uploadTutorial() {

        val args: ShareLinkFragmentArgs by navArgs()
        val title = args.title
        val materials = args.materials
        val time = args.time
        val description = args.description
        val link = args.link

        viewModel.uploadTutorialDetailsToFirestore(title, materials, time, description, link )
    }

    private fun showProgressBar() {
        publish.visibility = View.INVISIBLE
        createProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        publish.visibility = View.VISIBLE
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
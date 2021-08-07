package github.earth.homescreen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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

        nextbuttonone.setOnClickListener {
//            nextPage()
        }


        ivBackBtn.setOnClickListener {
            Toast.makeText(activity, "Tutorial Creation Cancelled", Toast.LENGTH_SHORT).show()
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

    //передача всех данных на следующую стр
//    private fun nextPage() {
//        viewModel.setTutorialImageUri()
//
//        val action = ShareFragmentPhotoDirections.actionSharePhotoScreenToShareInfoFragment(
//
//        )
//    }

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
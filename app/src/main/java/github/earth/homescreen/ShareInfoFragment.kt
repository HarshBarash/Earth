package github.earth.homescreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import github.earth.MainActivity
import github.earth.R
import github.earth.models.User
import github.earth.utils.*
import kotlinx.android.synthetic.main.fragment_shareinfo.*
import kotlinx.android.synthetic.main.fragment_shareinfo.ivBackBtn

class ShareInfoFragment  : Fragment(R.layout.fragment_shareinfo) {


    private lateinit var viewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).homeViewModel


        nextbuttontwo.setOnClickListener {
           //переход дальше
//            uploadTutorial()
        }

//        viewModel.uploadTutorialState.observe(viewLifecycleOwner, Observer {
//            when (it) {
//                is Resource.Success -> {
//                    hideProgressBar()
//                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
//                    this.findNavController()
//                        .navigate(R.id.action_shareInfoFragment_to_shareLinkFragment)
//                    viewModel.doneTutorialImageUri()
//                }
//                is Resource.Error -> {
//                    hideProgressBar()
//                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
//                }
//                is Resource.Loading -> {
//                    showProgressBar()
//                }
//            }
//            viewModel.doneTutorialState()
//        })
//
//        ivBackBtn.setOnClickListener {
//            Toast.makeText(activity, "Tutorial Creation Cancelled", Toast.LENGTH_SHORT).show()
//            this.findNavController().navigateUp()
//            viewModel.doneTutorialImageUri()
//        }
//    }

    //todo как тест все без bundle
//    private fun uploadTutorial() {
//        val title = etTutorialTitle.text.toString()
//        val materials = etTutorialMaterials.text.toString()
//        val time = etTutorialTime.text.toString().toInt()
//        val description = etTutorialDescription.text.toString()
//        val link = etTutorialLink.text.toString()

    }
//
//    private fun showProgressBar() {
//        nextbuttontwo.visibility = View.INVISIBLE
//        createProgressBar.visibility = View.VISIBLE
//    }
//
//    private fun hideProgressBar() {
//        nextbuttontwo.visibility = View.VISIBLE
//        createProgressBar.visibility = View.INVISIBLE
//    }


}


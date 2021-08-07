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
import kotlinx.android.synthetic.main.fragment_sharelink.*
import kotlinx.android.synthetic.main.fragment_sharelink.ivBackBtn

class ShareLinkFragment : Fragment(R.layout.fragment_sharelink) {


    class ShareInfoFragment  : Fragment(R.layout.fragment_shareinfo) {


        private lateinit var viewModel: HomeViewModel

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            viewModel = (activity as MainActivity).homeViewModel

            nextbuttonsecond.setOnClickListener {
                dataTransmission()
            }

            ivBackBtn.setOnClickListener {
                Toast.makeText(activity, "Tutorial Creation Cancelled", Toast.LENGTH_SHORT).show()
                this.findNavController().navigateUp()
            }
        }

        private fun dataTransmission() {

            val args: ShareLinkFragmentArgs by navArgs()
            val title = args.title
            val Materials = args.materials
            val Time = args.time

            val Description = etTutorialDescription.text.toString()
            val Link = etTutorialLink.text.toString()


            val action = ShareLinkFragmentDirections.actionShareLinkFragmentToSharePhotoScreen(
                title,
                Materials,
                Time,
                Description,
                Link
            )

            findNavController().navigate(action)
        }
    }
}
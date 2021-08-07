package github.earth.homescreen

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import github.earth.MainActivity
import github.earth.R
import github.earth.utils.Resource
import kotlinx.android.synthetic.main.fragment_shareinfo.*

class ShareInfoFragment  : Fragment(R.layout.fragment_shareinfo) {


    private lateinit var viewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).homeViewModel


        nextbuttonfirst.setOnClickListener {
           //переход дальше
            dataTransmission()
        }

        ivBackBtn.setOnClickListener {
            Toast.makeText(activity, "Tutorial Creation Cancelled", Toast.LENGTH_SHORT).show()
            this.findNavController().navigateUp()
        }
    }

    private fun dataTransmission() {

    val title = etTutorialTitle.text.toString()
    val materials = etTutorialMaterials.text.toString()
    val time = etTutorialTime.text.toString().toInt()

    val action = ShareInfoFragmentDirections.actionShareInfoFragmentToShareLinkFragment(title, materials, time)

    findNavController().navigate(action)

    }


}


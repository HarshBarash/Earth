package github.earth.homescreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import github.earth.R
import github.earth.models.Tutorial
import github.earth.utils.convertedDate
import kotlinx.android.synthetic.main.fragment_single_tutorial.*

class SingleTutorialFragment : Fragment(R.layout.fragment_single_tutorial) {

    private val args: SingleTutorialFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tutorial = args.tutorial
        setTutorialDetails(tutorial)

        ivBackBtn.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private fun setTutorialDetails(tutorial: Tutorial) {
        Glide.with(this).load(tutorial.profileImageUrl).placeholder(ivProfileImage.drawable).into(ivProfileImage)
        tvUsername.text = tutorial.username
        tvTimestamp.text = convertedDate(tutorial.timestamp)
        Glide.with(this).load(tutorial.tutorialImageUrl).placeholder(ivTutorialImage.drawable).into(ivTutorialImage)
        tvTitle.text = tutorial.title
        tvMaterials.text = tutorial.materials
        tvTutorial.text = tutorial.description


    }
}
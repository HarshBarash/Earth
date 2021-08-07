package github.earth.homescreen

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import github.earth.MainActivity
import github.earth.R
import github.earth.TutorialRecyclerViewAdapter
import github.earth.authscreen.LoginActivity
import github.earth.models.Tutorial
import github.earth.utils.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) {


    private lateinit var mFirebase: FirebaseHelper

    //    private lateinit var mAuth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var tutorialAdapter: TutorialRecyclerViewAdapter //todo


    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(LOG_HOME_FRAGMENT, "onCreate called")

        mFirebase = FirebaseHelper(requireActivity())
//        mAuth = FirebaseAuth.getInstance()


        //Кто-нибудь, скажите Антону что это здесь не нужно
        // нужно) Так как в большем кол-ве случаев юзер здесь, нежели в реге. Поэтому и линкаю сюда
        if (mFirebase.auth.currentUser == null) {
            val intent_toLogin = Intent(activity, LoginActivity::class.java)
            activity?.startActivity(intent_toLogin)
            activity?.finish()
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).homeViewModel

//        setCurrentUserDetails()
//
        setAllTutorials()


        btnPlusContent.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_shareInfoFragment)
        }
    }


    private fun setAllTutorials() {
        viewModel.getTutorialsState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    tutorialsProgressBar.visibility = View.INVISIBLE
                }
                is Resource.Error -> {
                    tutorialsProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    tutorialsProgressBar.visibility = View.VISIBLE
                }
            }
        })

        setupRecyclerView()

        viewModel.tutorialList.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d(LOG_HOMEVIEWMODEL, "setAllTutorials: $it")
                tutorialAdapter.differ.submitList(it)
            }
        })


        //OnClickListener
        tutorialAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("tutorial", it)
            }
            this.findNavController()
                .navigate(R.id.action_HomeFragment_to_singleTutorialFragment, bundle)
        }
    }

    private fun setupRecyclerView() {
        tutorialAdapter = TutorialRecyclerViewAdapter()
        rvPosts.apply {
            adapter = tutorialAdapter
            layoutManager = GridLayoutManager(activity, 2)

        }
    }


}





package github.earth.homescreen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import github.earth.MainActivity
import github.earth.R
import github.earth.TutorialRecyclerViewAdapter
import github.earth.utils.FirebaseHelper
import github.earth.utils.LOG_HOME_FRAGMENT
import github.earth.utils.LOG_HOME_VIEW_MODEL
import github.earth.utils.Resource
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.tutorial_item.*


class HomeFragment : Fragment(R.layout.fragment_home) {


    private lateinit var mFirebase: FirebaseHelper

    //    private lateinit var mAuth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var tutorialAdapter: TutorialRecyclerViewAdapter


    private lateinit var viewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(LOG_HOME_FRAGMENT, "onCreate called")

        mFirebase = FirebaseHelper(requireActivity())
//        mAuth = FirebaseAuth.getInstance()



        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            this.findNavController().navigate(R.id.action_HomeFragment_to_loginFragment)
        }



        //Кто-нибудь, скажите Антону что это здесь не нужно
        // нужно) Так как в большем кол-ве случаев юзер здесь, нежели в реге. Поэтому и линкаю сюда
//        if (mFirebase.auth.currentUser == null) {
//            val intent_toLogin = Intent(activity, LoginActivity::class.java)
//            activity?.startActivity(intent_toLogin)
//            activity?.finish()
//        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).homeViewModel

            //вывлдим спасибо юзеру
//        setCurrentUserDetails()

        setCurrentUserDetails()

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
                Log.d(LOG_HOME_VIEW_MODEL, "setAllTutorials: $it")
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

    private fun setCurrentUserDetails() {
        viewModel.getCurrentUserDetails()
        viewModel.username.observe(viewLifecycleOwner, Observer{
            tvUsername.text = it
        })
        viewModel.profileImageUri.observe(viewLifecycleOwner, Observer {
            Glide.with(this).load(it).placeholder(ivProfileImage.drawable).into(ivProfileImage)
        })
    }

}





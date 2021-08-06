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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import github.earth.MainActivity
import github.earth.R
import github.earth.authscreen.LoginActivity
import github.earth.models.Feed
import github.earth.utils.FirebaseHelper
import github.earth.utils.LOG_HOME_FRAGMENT
import github.earth.utils.ValueEventListenerAdapter
import github.earth.utils.loadImage
import kotlinx.android.synthetic.main.feed_item.view.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), View.OnClickListener {


    private lateinit var mFirebase: FirebaseHelper
//    private lateinit var mAuth: FirebaseAuth
    private lateinit var navController: NavController

    private lateinit var btnPlusContent: Button

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
        } else {
            mFirebase.database.child("Feed").child(mFirebase.auth.currentUser!!.uid)
                .addValueEventListener(ValueEventListenerAdapter {
                    val tutorials = it.children.map { it.getValue(Feed::class.java)!! }
                    feed_recycler.adapter = FeedAdapter(tutorials)
                    feed_recycler.layoutManager = LinearLayoutManager(requireContext())
                })
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).homeViewModel

        setCurrentUserDetails()

        setAllTutorials()



        fabAddPost.setOnClickListener {
            this.findNavController().navigate(R.id.action_homeFragment_to_createPostFragment)
        }
    }

    private fun setAllPosts() {
        viewModel.getPostsState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    postsProgressBar.visibility = View.INVISIBLE
                }
                is Resource.Error -> {
                    postsProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    postsProgressBar.visibility = View.VISIBLE
                }
            }
        })

        setupRecyclerView()

        viewModel.postList.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d(TAG, "setAllPosts: $it")
                blogAdapter.differ.submitList(it)
            }
        })


        //OnClickListener
        blogAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("post", it)
            }
            this.findNavController()
                .navigate(R.id.action_homeFragment_to_singlePostFragment, bundle)
        }
    }

    private fun setupRecyclerView() {
        blogAdapter = BlogRecyclerViewAdapter()
        rvPosts.apply {
            adapter = blogAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


}





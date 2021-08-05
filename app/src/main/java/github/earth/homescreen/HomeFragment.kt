package github.earth.homescreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import github.earth.R
import github.earth.authscreen.LoginActivity
import github.earth.models.Feed
import github.earth.utils.FirebaseHelper
import github.earth.utils.LOG_HOME_FRAGMENT
import github.earth.utils.ValueEventListenerAdapter
import github.earth.utils.loadImage
import kotlinx.android.synthetic.main.feed_item.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_sharephoto.view.*
import kotlinx.android.synthetic.main.fragment_sharephoto.view.tutorial_image

class HomeFragment : Fragment(), View.OnClickListener {


    private lateinit var mFirebase: FirebaseHelper
//    private lateinit var mAuth: FirebaseAuth
    private lateinit var navController: NavController

    private lateinit var btnPlusContent: Button

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
                .addValueEventListener(ValueEventListenerAdapter{
                    val tutorials = it.children.map { it.getValue(Feed::class.java)!! }
                    feed_recycler.adapter = FeedAdapter(tutorials)
                    feed_recycler.layoutManager = LinearLayoutManager(requireContext())
                })
        }




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(LOG_HOME_FRAGMENT, "onCreate called")
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        btnPlusContent = rootView.findViewById(R.id.btnPlusContent)
        btnPlusContent.setOnClickListener(this)


        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.v(LOG_HOME_FRAGMENT, "onDestroyView called")

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnPlusContent -> {
                findNavController().navigate(R.id.action_HomeFragment_to_SharePhotoScreen)
            }
        }
    }

}

class FeedAdapter(private val tutorials: List<Feed>)
    : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_item, parent, false)
       return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tutorial = tutorials[position]
        with(holder) {
            view.user_photo_image.loadImage(tutorial.photo)
            view.username_text.text = tutorial.username
            view.tutorial_image.loadImage(tutorial.image)
            view.title_feed.text = tutorial.title
        }
    }

    override fun getItemCount() = tutorials.size
    }

    private fun ImageView.loadImage(image: String?) {
        Glide.with(this).load(image).centerCrop().into(this)
    }

package github.earth.homescreen

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import github.earth.R
import github.earth.authscreen.ValueEventListenerAdapter
import github.earth.models.User
import github.earth.utils.FirebaseHelper
import github.earth.utils.LOG_PROFILE_FRAGMENT
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var mUser : User


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        Log.d(LOG_PROFILE_FRAGMENT, "onCreate")

        mFirebaseHelper = FirebaseHelper(activity)
        mFirebaseHelper.currentUserReference().addValueEventListener(ValueEventListenerAdapter {
            mUser = it.getValue(User::class.java)!!
            username_text.text = mUser.username


        })

        images_recycler.layoutManager = GridLayoutManager(activity, 2)
        mFirebaseHelper.database.child("images").child(mFirebaseHelper.auth.currentUser!!.uid)
            .addValueEventListener(ValueEventListenerAdapter {
                val images = it.children.map { it.getValue((String::class.java)) }
                images_recycler.adapter = ImagesAdapter(images)
            })

        return view
    }
}


class ImagesAdapter(private val images: List<String?>) :
    RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {
    class ViewHolder(val image: ImageView) : RecyclerView.ViewHolder(image)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val image = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false) as ImageView
        return ViewHolder(image)
    }


    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        images[position]?.let { holder.image.loadImage(it) }
    }

    private fun ImageView.loadImage(image: String) {
        Picasso.get()
            .load(image)
            .into(this);
    }
}

class SquareImageView(context: Context, attrs: AttributeSet) :
    androidx.appcompat.widget.AppCompatImageView (context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
    }
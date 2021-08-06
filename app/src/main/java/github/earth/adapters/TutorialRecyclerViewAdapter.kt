package github.earth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import github.earth.models.Tutorial
import github.earth.utils.convertedDate
import kotlinx.android.synthetic.main.fragment_single_tutorial.view.*
import kotlinx.android.synthetic.main.tutorial_item.view.*
import kotlinx.android.synthetic.main.tutorial_item.view.ivProfileImage
import kotlinx.android.synthetic.main.tutorial_item.view.ivTutorialImage
import kotlinx.android.synthetic.main.tutorial_item.view.tvTitle
import kotlinx.android.synthetic.main.tutorial_item.view.tvUsername


class TutorialRecyclerViewAdapter : RecyclerView.Adapter<TutorialRecyclerViewAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var differCallback = object : DiffUtil.ItemCallback<Tutorial>() {
        override fun areItemsTheSame(oldItem: Tutorial, newItem: Tutorial): Boolean {
            return oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: Tutorial, newItem: Tutorial): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.tutorial_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val tutorial = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(tutorial.profileImageUrl)
                .into(ivProfileImage)
            tvUsername.text = tutorial.username
            tvTimestamp.text = convertedDate(tutorial.timestamp)
            Glide.with(this).load(tutorial.tutorialImageUrl).placeholder(R.color.colorPrimary)
                .into(ivTutorialImage)
            tvTitle.text = tutorial.title
            setOnClickListener {
                onItemClickListener?.let {
                    it(tutorial)
                }
            }
        }
    }

    private var onItemClickListener: ((Tutorial) -> Unit)? = null

    fun setOnItemClickListener(listener: (Tutorial) -> Unit) {
        onItemClickListener = listener
    }
}
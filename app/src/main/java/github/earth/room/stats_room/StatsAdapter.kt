package github.earth.room.stats_room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import github.earth.R

class StatsAdapter : RecyclerView.Adapter<StatsAdapter.MyViewHolder>() {

    private var statsList = emptyList<StatsRoom>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = statsList[position]
        var collected_waste: TextView = holder.itemView.findViewById(R.id.txt_collected)
        var count_visited: TextView = holder.itemView.findViewById(R.id.txt_visited_places)
        var rank: TextView = holder.itemView.findViewById(R.id.txt_rank)
        var saved_trees: TextView = holder.itemView.findViewById(R.id.txt_saved_trees)

        collected_waste.text = currentItem.collected_waste.toString()
        count_visited.text = currentItem.visited_places.toString()
        rank.text = currentItem.rank.toString()
        saved_trees.text = (currentItem.collected_waste.toDouble()/60.0).toString()

        // ----- здесь можно ( нужно ) добавить действия при обновлении ------ //


    }

    fun setData(stats: List<StatsRoom>){
        this.statsList = stats
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return statsList.size
    }


}
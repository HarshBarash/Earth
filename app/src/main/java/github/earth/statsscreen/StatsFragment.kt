package github.earth.statsscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import github.earth.room.stats_room.StatsAdapter
import github.earth.room.stats_room.StatsRoom
import github.earth.room.stats_room.StatsViewModel
import github.earth.R


class StatsFragment : Fragment() {

    private lateinit var mStatsViewModel: StatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_stats, container, false)
        mStatsViewModel = ViewModelProvider(this).get(StatsViewModel::class.java)
        insertDataToDatabase(0, 0, 120)

        //RecyclerView
        val adapter = StatsAdapter()
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //StatsViewModel
        mStatsViewModel = ViewModelProvider(this).get(StatsViewModel::class.java)
        mStatsViewModel.readAllData.observe(viewLifecycleOwner, Observer { stats ->
            adapter.setData(stats)
        })

        return view
    }

    fun insertDataToDatabase(collected_waste: Int, visited_places: Int, rank: Int) {

        //create user object
        val stats = StatsRoom(
            1,
            collected_waste,
            visited_places,
            rank)

        //add data to db
        mStatsViewModel = ViewModelProvider(this).get(StatsViewModel::class.java)
        mStatsViewModel.addStats(stats)

    }

    fun updateItem(fragment: Fragment, collected_waste: Int, visited_places: Int, rank: Int) {
        //create stats object
        val updateStats = StatsRoom(
            1,
            collected_waste,
            visited_places,
            rank
        )
        //update stats
        mStatsViewModel = ViewModelProvider(fragment).get(StatsViewModel::class.java)
        mStatsViewModel.updateStats(updateStats)

    }

    fun readItemAndUpdate(fragment: Fragment) {
        mStatsViewModel = ViewModelProvider(fragment).get(StatsViewModel::class.java)
        mStatsViewModel.readAllData.observe(fragment, Observer { stats ->
            stats.forEach { it ->
                var collected_waste = it.collected_waste
                var visited_places = it.visited_places
                var rank = it.rank
                if (rank > 1) {
                    updateItem(fragment, collected_waste + 1, visited_places + 1, rank - 1)
                }else{
                    updateItem(fragment, collected_waste + 1, visited_places + 1, rank)
                }
            }
        })


    }

    fun deleteAllStats() {
        mStatsViewModel = ViewModelProvider(this).get(StatsViewModel::class.java)
        mStatsViewModel.deleteAllStats() //удалить всё
    }

}
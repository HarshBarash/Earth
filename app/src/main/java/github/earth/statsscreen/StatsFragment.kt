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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_stats, container, false)
        mStatsViewModel = ViewModelProvider(this).get(StatsViewModel::class.java)
        insertDataToDatabase()

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
    private fun insertDataToDatabase() {

        //create user object
        val stats = StatsRoom(
            1,
            0,
            0,
            120)

        //add data to db
        mStatsViewModel.addStats(stats)

    }

    private fun updateItem() {
        //create stats object
        val updateStats = StatsRoom(
            1,
            20,
            20,
            22
        )
        //update stats
        mStatsViewModel.updateStats(updateStats)

    }


    private fun deleteAllStats() {
        mStatsViewModel.deleteAllStats() //удалить всё
    }

}
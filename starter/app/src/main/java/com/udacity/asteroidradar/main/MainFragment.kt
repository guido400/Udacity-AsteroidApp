package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.DateUtils
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.db.AsteroidDatabase

/**
 * Main fragment being used to present a listing of Asteroids fetched from Nasa via the offline cache
 *
 * @constructor Create empty Main fragment
 */
class MainFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    val asteroidAdapter = AsteroidAdapter(AsteroidListener { asteroid ->
        val action = MainFragmentDirections.actionShowDetail(asteroid)
        findNavController().navigate(action)
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)

        binding.lifecycleOwner = this
        val application = requireNotNull(this.activity).application
        val viewModelFactory = MainViewModelFactory(application)
        viewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(MainViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.asteroidList.observe(viewLifecycleOwner, Observer {
            reSubmitAdapterData(it)
        })


        binding.asteroidRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = asteroidAdapter
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * On options item selected filtering of asteroidItems will be triggered via Livedata in the viewmodel
     *
     * @param menuItem
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.asteroidFilter = when (item.itemId) {
            R.id.show_week_menu -> AsteroidFilter.WEEK
            R.id.show_today_menu -> AsteroidFilter.DAY
            R.id.show_saved_menu -> AsteroidFilter.SAVED
            else -> AsteroidFilter.SAVED
        }

        reSubmitAdapterData(viewModel.asteroidList.value!!)
        return true
    }

    /**
     * Resubmit adapter data either when asteroid data is updated from the network or filtering is changed by the user
     *
     * @param list
     */
    private fun reSubmitAdapterData(list: List<Asteroid>) {
        when (viewModel.asteroidFilter) {
            AsteroidFilter.SAVED -> asteroidAdapter.submitList(list)
            AsteroidFilter.DAY -> asteroidAdapter.submitList(list.filter { asteroid ->
                DateUtils.isDateToday(
                    asteroid.closeApproachDate
                )
            })
            AsteroidFilter.WEEK -> asteroidAdapter.submitList(list.filter { asteroid ->
                DateUtils.isDateThisWeek(
                    asteroid.closeApproachDate
                )
            })
        }
    }
}

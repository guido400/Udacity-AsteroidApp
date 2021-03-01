package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.db.AsteroidDatabase
import com.udacity.asteroidradar.db.AsteroidEntity
import com.udacity.asteroidradar.db.asDomainModel

class MainFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val application = requireNotNull(this.activity).application
        val dataSource = AsteroidDatabase.getInstance(application.applicationContext).asteroidDao
        val viewModelFactory = MainViewModelFactory(dataSource, application)
        val viewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(MainViewModel::class.java)

        binding.viewModel = viewModel

        val asteroidAdapter = AsteroidAdapter(AsteroidListener { asteroid ->
            Toast.makeText(context, asteroid.id.toString(), Toast.LENGTH_SHORT).show()
            //val action = MainFragmentDirections.actionShowDetail(asteroid)
            //findNavController().navigate(action)
        })

        viewModel.asteroidList.observe(viewLifecycleOwner, Observer {
            val asteroidList = it.asDomainModel()
            asteroidAdapter.submitList(asteroidList)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}

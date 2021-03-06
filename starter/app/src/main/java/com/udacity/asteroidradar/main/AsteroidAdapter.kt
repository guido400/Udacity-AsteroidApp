package com.udacity.asteroidradar.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidListItemBinding

/**
 * Asteroid adapter class for binding asteroid data to recyclerview items
 *
 * @property clickListener for recyclerview items
 * @constructor Create empty Asteroid adapter
 */
class AsteroidAdapter(val clickListener: AsteroidListener) :
    ListAdapter<Asteroid, AsteroidAdapter.ViewHolder>(AsteroidDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)!!
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor(
        val binding: AsteroidListItemBinding,
        val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Asteroid, clickListener: AsteroidListener) {
            val imageResource =
                if (item.isPotentiallyHazardous) R.drawable.ic_status_potentially_hazardous else R.drawable.ic_status_normal
            binding.hazardousIcon.setImageResource(imageResource)
            val isHazardousDescription =
                if (item.isPotentiallyHazardous) context.getString(R.string.hazardous) else context.getString(
                    R.string.not_hazardous
                )
            binding.asteroidItemLayout.contentDescription = context.getString(
                R.string.asteroid_content_description,
                item.codename,
                item.closeApproachDate,
                isHazardousDescription
            )

            binding.asteroidName.text = item.codename
            binding.asteroidDate.text = item.closeApproachDate
            binding.asteroid = item

            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AsteroidListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, parent.context)
            }
        }
    }
}

class AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem == newItem
    }
}

class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}




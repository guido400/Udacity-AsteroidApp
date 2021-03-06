package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.api.ApiStatus
import com.udacity.asteroidradar.api.DailyImage


/**
 * Bind asteroid status icon
 *
 * @param imageView
 * @param isHazardous
 */
@BindingAdapter("statusIcon")
fun bindAsteroidStatusIcon(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

/**
 * Bind details status image
 *
 * @param imageView
 * @param isHazardous
 */
@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = imageView.context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = imageView.context.getString(R.string.not_hazardous_asteroid_image)
    }
}

/**
 * Bind text view to astronomical unit
 *
 * @param textView
 * @param number
 */
@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

/**
 * Bind text view to km unit
 *
 * @param textView
 * @param number
 */
@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

/**
 * Bind text view to display velocity
 *
 * @param textView
 * @param number
 */
@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

/**
 * when loading status from the network is updated, reflect in loading spinner visibility
 *
 * @param statusImageView
 * @param status
 */
@BindingAdapter("apiStatus")
fun bindStatus(statusImageView: ImageView, status: ApiStatus?) {
    when (status) {
        ApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        ApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        ApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

/**
 * Set image on main fragment when a new image is fetched from the network
 *
 * @param asteroidImageView
 * @param dailyImage
 */
@BindingAdapter("asteroidImage")
fun bindStatus(asteroidImageView: ImageView, dailyImage:DailyImage?) {
    if (dailyImage != null) {
        if (dailyImage.mediaType == "image") {
            Picasso.get().load(dailyImage.url).into(asteroidImageView)
            asteroidImageView.contentDescription = "image of the day, the title is:${dailyImage.title}"
        }
    }
}


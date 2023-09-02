package com.luther.github.ui.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luther.github.data.model.MovieDetailsOtherRatings
import com.luther.github.databinding.MovieDetailsOtherRatingsItemBinding
import timber.log.Timber

class MovieDetailsOtherRatingsAdapter :
    ListAdapter<MovieDetailsOtherRatings, MovieDetailsOtherRatingsAdapter.MovieDetailsOtherRatingsViewHolder>(
        MovieDetailsOtherRatingsItemDiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieDetailsOtherRatingsViewHolder {
        return MovieDetailsOtherRatingsViewHolder(
            MovieDetailsOtherRatingsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieDetailsOtherRatingsViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class MovieDetailsOtherRatingsViewHolder(private val binding: MovieDetailsOtherRatingsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movieDetailsOtherRatings: MovieDetailsOtherRatings) {
            Timber.d("source = ${movieDetailsOtherRatings.source}")
            Timber.d("rating = ${movieDetailsOtherRatings.value}")
            binding.apply {
                sourceName.text = movieDetailsOtherRatings.source
                rating.text = movieDetailsOtherRatings.value
            }
        }
    }
}

class MovieDetailsOtherRatingsItemDiffCallback : DiffUtil.ItemCallback<MovieDetailsOtherRatings>() {
    override fun areItemsTheSame(
        oldItem: MovieDetailsOtherRatings,
        newItem: MovieDetailsOtherRatings
    ): Boolean {
        return (oldItem.source == newItem.source) && (oldItem.value == newItem.value)
    }

    override fun areContentsTheSame(
        oldItem: MovieDetailsOtherRatings,
        newItem: MovieDetailsOtherRatings
    ): Boolean {
        return oldItem == newItem
    }
}

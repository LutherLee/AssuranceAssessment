package com.luther.github.ui.movie.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luther.github.R
import com.luther.github.data.model.MovieList
import com.luther.github.databinding.MovieItemBinding
import com.luther.github.ui.movie.MovieListFragmentDirections
import com.luther.github.util.getCircularProgressDrawable
import javax.inject.Inject

class MovieListPagingAdapter @Inject constructor() :
    PagingDataAdapter<MovieList, MovieListPagingAdapter.MovieItemViewHolder>(MovieItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        return MovieItemViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class MovieItemViewHolder(
        private val binding: MovieItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieList) {
            checkPictureSourceAndLoadIt(movie.posterUrl)
            setupListener(binding.root, movie.id)
            binding.movieTitle.text = movie.title
        }

        private fun checkPictureSourceAndLoadIt(imageUrl: String) {
            val requestBuilder = Glide.with(binding.root).asDrawable().sizeMultiplier(0.1f)
            Glide.with(binding.root)
                .load(imageUrl)
                .fitCenter()
                .placeholder(getCircularProgressDrawable())
                .thumbnail(requestBuilder)
                .error(R.drawable.custom_default_image)
                .into(binding.moviePoster)
        }

        private fun setupListener(view: View, movieID: String) {
            view.setOnClickListener {
                Navigation.findNavController(view).navigate(
                    MovieListFragmentDirections.actionMovieListFragmentToMovieDetailsFragment(
                        movieID
                    )
                )
            }
        }
    }
}

class MovieItemDiffCallback : DiffUtil.ItemCallback<MovieList>() {
    override fun areItemsTheSame(oldItem: MovieList, newItem: MovieList): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.title == newItem.title &&
                oldItem.posterUrl == newItem.posterUrl &&
                oldItem.pageIndex == newItem.pageIndex
    }

    override fun areContentsTheSame(oldItem: MovieList, newItem: MovieList): Boolean {
        return oldItem == newItem
    }
}
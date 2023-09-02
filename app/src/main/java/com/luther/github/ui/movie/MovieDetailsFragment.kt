package com.luther.github.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luther.github.data.model.Movie
import com.luther.github.data.network.NetworkResponse
import com.luther.github.databinding.FragmentMovieDetailsBinding
import com.luther.github.ui.movie.adapter.MovieDetailsOtherRatingsAdapter
import com.luther.github.util.collectWithLifecycle
import com.luther.github.util.getCircularProgressDrawable
import com.luther.github.util.onDestroyNullable
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.glide.transformations.BlurTransformation

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var binding by onDestroyNullable<FragmentMovieDetailsBinding>()
    private val movieViewModel: MovieViewModel by viewModels()
    private val args: MovieDetailsFragmentArgs by navArgs()
    private var movieDetailsOtherRatingsAdapter: MovieDetailsOtherRatingsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Fetch movieID from navArgs and update viewModel
        movieViewModel.onCurrentlyViewedMovieDetailChanged(args.movieID)
        setupRecyclerview()
        setupListener()
        subscribeUI()
    }

    private fun setupRecyclerview() {
        movieDetailsOtherRatingsAdapter = MovieDetailsOtherRatingsAdapter()
        movieDetailsOtherRatingsAdapter!!.submitList(null)

        binding.movieDetailsOtherRatingsRecyclerview.apply {
            adapter = movieDetailsOtherRatingsAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            LinearSnapHelper().attachToRecyclerView(this)
        }
    }

    private fun subscribeUI() {
        movieViewModel.movieDetails.collectWithLifecycle(viewLifecycleOwner) { movieDetailsNetworkResponse ->
            when (movieDetailsNetworkResponse) {
                is NetworkResponse.Success -> {
                    val movie = movieDetailsNetworkResponse.body
                    showMovieDetailsUI(movie)
                    movieDetailsOtherRatingsAdapter!!.submitList(movie.otherRatings)
                }
                else -> showErrorDialog()
            }
        }
    }

    private fun setupListener() {
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
    }

    private fun showErrorDialog() {
        MaterialAlertDialogBuilder(requireContext(), com.luther.github.R.style.DefaultAlertDialogTheme)
            .setTitle(getString(com.luther.github.R.string.error_text))
            .setMessage(getString(com.luther.github.R.string.error_fetching_movie_details))
            .setPositiveButton(getString(com.luther.github.R.string.confirm_text)) { _, _ ->
                findNavController().navigateUp()
            }
            .create()
            .show()
    }

    private fun showMovieDetailsUI(movie: Movie) {
        updatePosterImage(movie.posterUrl)
        updatePosterBackgroundImage(movie.posterUrl)
        binding.apply {
            movieTitle.text = movie.title
            movieGenres.text = movie.genre
            plotSummaryDetails.text = movie.plot
            movieRatingScore.text = getString(com.luther.github.R.string.movie_rating_score, movie.rating)
            movieTotalNoOfRating.text = getString(com.luther.github.R.string.movie_total_no_of_ratings, movie.votes)

            val movieRating = movie.rating.toFloatOrNull()
            movieRatingBar.rating = if (movieRating != null) {
                (movieRating / 2)
            } else {
                0F
            }
        }
    }

    private fun updatePosterImage(moviePosterUrl: String) {
        val requestBuilder = Glide.with(binding.root).asDrawable().sizeMultiplier(0.1f)
        Glide.with(binding.root)
            .load(moviePosterUrl)
            .fitCenter()
            .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .placeholder(getCircularProgressDrawable())
            .thumbnail(requestBuilder)
            .error(com.luther.github.R.drawable.custom_default_image)
            .into(binding.moviePoster)
    }

    private fun updatePosterBackgroundImage(moviePosterUrl: String) {
        val requestBuilder = Glide.with(binding.root).asDrawable().sizeMultiplier(0.1f)
        Glide.with(binding.root)
            .load(moviePosterUrl)
            .centerInside()
            .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 1)))
            .placeholder(getCircularProgressDrawable())
            .thumbnail(requestBuilder)
            .error(com.luther.github.R.drawable.custom_default_image)
            .into(binding.moviePosterBackground)
    }
}
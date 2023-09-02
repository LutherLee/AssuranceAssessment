package com.luther.github.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.luther.github.databinding.FragmentMovieListBinding
import com.luther.github.ui.movie.paging.MovieListLoadStateAdapter
import com.luther.github.ui.movie.paging.MovieListPagingAdapter
import com.luther.github.util.RecyclerViewGridSpacingItemDecoration
import com.luther.github.util.collectWithLifecycle
import com.luther.github.util.onDestroyNullable
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private var binding by onDestroyNullable<FragmentMovieListBinding>()
    private val movieViewModel: MovieViewModel by viewModels()

    @Inject
    lateinit var movieListPagingAdapter: MovieListPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        subscribeUI()
    }

    private fun setupRecyclerView() {
        binding.movieListRecyclerview.apply {
            itemAnimator = null
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = movieListPagingAdapter
            setHasFixedSize(true)
            addItemDecoration(
                RecyclerViewGridSpacingItemDecoration(2, 60, false)
            )
        }
    }

    private fun setupListeners() {
        binding.movieSearchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    movieViewModel.onSearchKeywordChanged(it)
                    binding.movieListRecyclerview.scrollToPosition(0)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
        })

        movieListPagingAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.NotLoading -> {
                    when (movieListPagingAdapter.itemCount <= 0) {
                        true -> onEmptyResult()
                        false -> onResultFound()
                    }
                }
                is LoadState.Loading -> onLoadingResult()
                is LoadState.Error -> {
                    val error = when {
                        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                        else -> null
                    }
                    error?.let { Timber.d("Paging Load Failed: ${it.error.message}") }
                }
            }
        }
    }

    private fun subscribeUI() {
        movieViewModel.movieList.collectWithLifecycle(viewLifecycleOwner) { pagingData ->
            movieListPagingAdapter.submitData(pagingData)
        }

        movieListPagingAdapter.withLoadStateFooter(
            MovieListLoadStateAdapter(movieListPagingAdapter::retry)
        )
    }

    private fun onEmptyResult() {
        binding.noResultFound.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun onResultFound() {
        binding.progressBar.visibility = View.GONE
        binding.noResultFound.visibility = View.GONE
    }

    private fun onLoadingResult() {
        binding.noResultFound.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }
}
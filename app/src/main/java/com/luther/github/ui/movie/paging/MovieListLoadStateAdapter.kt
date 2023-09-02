package com.luther.github.ui.movie.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luther.github.R
import com.luther.github.databinding.ReposLoadStateFooterViewItemBinding

class MovieListLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<MovieListLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: MovieListLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): MovieListLoadStateViewHolder {
        return MovieListLoadStateViewHolder.create(parent, retry)
    }
}

class MovieListLoadStateViewHolder(
    private val binding: ReposLoadStateFooterViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): MovieListLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.repos_load_state_footer_view_item, parent, false)
            val binding = ReposLoadStateFooterViewItemBinding.bind(view)
            return MovieListLoadStateViewHolder(binding, retry)
        }
    }
}
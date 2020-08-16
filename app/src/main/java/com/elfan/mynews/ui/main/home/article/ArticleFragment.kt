package com.elfan.mynews.ui.main.home.article

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elfan.mynews.R
import com.elfan.mynews.models.Article
import com.elfan.mynews.models.Category
import com.elfan.mynews.utils.extensions.gone
import com.elfan.mynews.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_article.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private val articleViewModel: ArticleViewModel by viewModel()

    companion object {
        fun instance(category: Category?): ArticleFragment {
            return if (category == null) {
                ArticleFragment()
            } else {
                val args = Bundle()
                args.putParcelable("category", category)
                ArticleFragment().apply {
                    arguments = args
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        observer()
    }

    private fun setUpUI() {
        requireView().recycler_view.apply {
            adapter = ArticleAdapter(mutableListOf(), requireActivity())
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun observer() {
        articleViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
        articleViewModel.listenToArticles().observe(viewLifecycleOwner, Observer { handleArticles(it) })
    }

    private fun handleArticles(it: List<Article>) {
        requireView().recycler_view.adapter?.let { adapter ->
            if (adapter is ArticleAdapter) {
                adapter.changelist(it)
            }
        }
    }

    private fun handleUiState(it: ArticleState) {
        when (it) {
            is ArticleState.Loading -> handleLoading(it.state)
            is ArticleState.ShowToast -> toast(it.message)
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) {
            requireView().shimmer_layout.visible()
            requireView().shimmer_layout.startShimmer()
        } else {
            requireView().shimmer_layout.stopShimmer()
            requireView().shimmer_layout.gone()
        }
    }

    private fun fetchArticles() {
        arguments?.let {
            articleViewModel.fetchArticlesByCategory(getPassedCategory()?.id.toString())
        } ?: kotlin.run {
            articleViewModel.fetchAllArticles()
        }
    }

    private fun toast(message: String) = Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
    private fun getPassedCategory(): Category? = arguments!!.getParcelable("category")
    override fun onResume() {
        super.onResume()
        fetchArticles()
    }
}
package com.elfan.mynews.ui.main.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.elfan.mynews.R
import com.elfan.mynews.models.Category
import com.elfan.mynews.ui.main.home.article.ArticleFragment
import com.elfan.mynews.utils.extensions.gone
import com.elfan.mynews.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
    }
    private fun observer() {
        homeViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
        homeViewModel.listenToCategories().observe(viewLifecycleOwner, Observer { handleCategories(it) })
    }

    private fun handleCategories(it: List<Category>) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(ArticleFragment.instance(null), "semua")
        it.forEach { category ->
            adapter.addFragment(ArticleFragment.instance(category), category.category!!)
        }
        requireView().view_pager.adapter = adapter
        requireView().tab_layout.setupWithViewPager(requireView().view_pager)
    }


    private fun handleUiState(it: HomeState) {
        when (it) {
            is HomeState.Loading -> handleLoading(it.state)
            is HomeState.ShowToast -> toast(it.message)
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

    private fun toast(m: String) = Toast.makeText(requireActivity(), m, Toast.LENGTH_SHORT).show()

    class ViewPagerAdapter(manager: FragmentManager) :
        FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val fragments: MutableList<Fragment> = ArrayList()
        private val titles: MutableList<String> = ArrayList()

        override fun getItem(position: Int): Fragment = fragments[position]

        override fun getCount(): Int = fragments.size

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? = titles[position]
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.fetchCategories()
    }
}
package com.anikinkirill.powerfulandroidapp.ui.main.blog

import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.models.BlogPost
import com.anikinkirill.powerfulandroidapp.persitence.BlogQueryUtils.Companion.BLOG_FILTER_DATE_UPDATED
import com.anikinkirill.powerfulandroidapp.persitence.BlogQueryUtils.Companion.BLOG_FILTER_USERNAME
import com.anikinkirill.powerfulandroidapp.persitence.BlogQueryUtils.Companion.BLOG_ORDER_ASC
import com.anikinkirill.powerfulandroidapp.ui.DataState
import com.anikinkirill.powerfulandroidapp.ui.main.blog.BlogListAdapter.Interaction
import com.anikinkirill.powerfulandroidapp.ui.main.blog.state.BlogViewState
import com.anikinkirill.powerfulandroidapp.ui.main.blog.viewmodel.*
import com.anikinkirill.powerfulandroidapp.util.ErrorHandling
import com.anikinkirill.powerfulandroidapp.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_blog.*

class BlogFragment : BaseBlogFragment(), Interaction, SwipeRefreshLayout.OnRefreshListener {

    companion object {
        private const val TAG = "AppDebug_BlogFragment"
    }

    private lateinit var recyclerAdapter: BlogListAdapter
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        swipe_refresh.setOnRefreshListener(this)
        initBlogListRecyclerView()
        subscribeObservers()
        if (savedInstanceState == null) {
            viewModel.loadFirstPage()
        }
    }

    private fun onBlogSearchOrFilter() {
        viewModel.loadFirstPage().let {
            resetUI()
        }
    }

    private fun resetUI() {
        blog_post_recyclerview.smoothScrollToPosition(0)
        onDataStateChangeListener.hideSoftKeyboard()
        focusable_view.requestFocus()
    }

    private fun initBlogListRecyclerView() {
        blog_post_recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingItemDecoration)
            addItemDecoration(topSpacingItemDecoration)
            recyclerAdapter = BlogListAdapter(
                interaction = this@BlogFragment,
                requestManager = requestManager
            )

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        viewModel.nextPage()
                    }
                }
            })

            adapter = recyclerAdapter
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState?.let {
                handlePagination(it)
                onDataStateChangeListener.onDataStateChange(it)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.let {
                recyclerAdapter.apply {
                    preloadGlideImages(
                        requestManager,
                        it.blogFields.blogList
                    )
                    submitList(
                        list = it.blogFields.blogList,
                        isQueryExhausted = it.blogFields.isQueryExhausted
                    )
                }
            }
        })
    }

    private fun initSearchView(menu: Menu) {
        activity?.apply {
            val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
            searchView = menu.findItem(R.id.action_search).actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.maxWidth = Integer.MAX_VALUE
            searchView.setIconifiedByDefault(true)
            searchView.isSubmitButtonEnabled = true
        }

        // case 1: WHEN USER CLICKS ON SUBMIT BUTTON NEAR KEYBOARD
        val searchPlate = searchView.findViewById(R.id.search_src_text) as EditText
        searchPlate.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchQuery = v.text.toString()
                viewModel.setQuery(searchQuery).let {
                    onBlogSearchOrFilter()
                }
            }
            true
        }

        // case 2: WHEN USER CLICKS ON SUBMIT BUTTON IN SEARCH VIEW TOOLBAR
        searchView.findViewById<View>(R.id.search_go_btn).setOnClickListener {
            val searchQuery = searchPlate.text.toString()
            viewModel.setQuery(searchQuery).let {
                onBlogSearchOrFilter()
            }
        }
    }

    private fun handlePagination(dataState: DataState<BlogViewState>) {
        dataState.data?.let {
            it.data?.let {
                it.getContentIfNotHandled()?.let {
                    viewModel.handleIncomingBlogListData(it)
                }
            }
        }

        dataState.error?.let { event ->
            event.peekContent().response.message?.let {
                if (ErrorHandling.isPaginationDone(it)) {
                    event.getContentIfNotHandled()
                    viewModel.setQueryExhausted(true)
                }
            }
        }
    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        viewModel.setBlogPost(item)
        findNavController().navigate(R.id.action_blogFragment_to_viewBlogFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Avoid memory leaks when view is destroyed
        blog_post_recyclerview.adapter = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        initSearchView(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter_settings -> {
                showFilterOptions()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRefresh() {
        onBlogSearchOrFilter()
        swipe_refresh.isRefreshing = false
    }

    private fun showFilterOptions() {
        // 0. show dialog
        activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .customView(R.layout.layout_blog_filter)
            val view = dialog.getCustomView()

            // 1. highlight previous options from SharedPreferences
            val filter = viewModel.getBlogFilter()
            val order = viewModel.getBlogOrder()
            if (filter == BLOG_FILTER_DATE_UPDATED) {
                view.findViewById<RadioGroup>(R.id.filter_group).check(R.id.filter_date)
            } else {
                view.findViewById<RadioGroup>(R.id.filter_group).check(R.id.filter_author)
            }

            if (order == BLOG_ORDER_ASC) {
                view.findViewById<RadioGroup>(R.id.order_group).check(R.id.filter_asc)
            } else {
                view.findViewById<RadioGroup>(R.id.order_group).check(R.id.filter_desc)
            }

            // 2. listen for newly applied filters
            view.findViewById<TextView>(R.id.positive_button).setOnClickListener {
                val selectedFilter = view.findViewById<RadioButton>(
                    view.findViewById<RadioGroup>(R.id.filter_group).checkedRadioButtonId
                )
                val selectedOrder = view.findViewById<RadioButton>(
                    view.findViewById<RadioGroup>(R.id.order_group).checkedRadioButtonId
                )
                var filterOption = BLOG_FILTER_DATE_UPDATED
                if (selectedFilter.text.toString() == getString(R.string.filter_author)) {
                    filterOption = BLOG_FILTER_USERNAME
                }
                var orderOption = ""
                if (selectedOrder.text.toString() == getString(R.string.filter_desc)) {
                    orderOption = "-"
                }

                // 3. set the filter and order in the viewmodel
                // 4. save to SharedPreferences
                viewModel.saveFilterOptions(filterOption, orderOption).let {
                    viewModel.setBlogFilter(filterOption)
                    viewModel.setBlogOrder(orderOption)
                    onBlogSearchOrFilter()
                }
                dialog.dismiss()
            }

            view.findViewById<TextView>(R.id.negative_button).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}
package com.anikinkirill.powerfulandroidapp.ui.main.blog.viewmodel

import android.util.Log
import com.anikinkirill.powerfulandroidapp.ui.main.blog.state.BlogStateEvent.BlogSearchEvent
import com.anikinkirill.powerfulandroidapp.ui.main.blog.state.BlogViewState

/**
 * Sets page value equals 1, so resetting
 * the current page in BlogFragment
 */

fun BlogViewModel.resetPage() {
    val update = getCurrentViewStateOrNew()
    update.blogFields.page = 1
    setViewState(update)
}

/**
 * First, should set query is being in progress,
 * because loading some page.
 *
 * Second, query cannot be exhausted,
 * because loading the first page
 *
 * Third, reset page for value equals 1
 *
 * Fourth, set BlogSearchEvent to trigger
 * searching
 */

fun BlogViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    setStateEvent(BlogSearchEvent())
}

fun BlogViewModel.incrementPageNumber() {
    val update = getCurrentViewStateOrNew()
    val currentPage = update.copy().blogFields.page
    update.blogFields.page = currentPage + 1
    setViewState(update)
}

fun BlogViewModel.nextPage() {
    if (!getIsQueryInProgress() && !getIsQueryExhausted()) {
        Log.d("AppDebug_Pagination", "nextPage: trying to load next page...")
        incrementPageNumber()
        setQueryInProgress(true)
        setStateEvent(BlogSearchEvent())
    }
}

fun BlogViewModel.handleIncomingBlogListData(viewState: BlogViewState) {
    setQueryExhausted(viewState.blogFields.isQueryExhausted)
    setQueryInProgress(viewState.blogFields.isQueryInProgress)
    setBlogPostList(viewState.blogFields.blogList)
}
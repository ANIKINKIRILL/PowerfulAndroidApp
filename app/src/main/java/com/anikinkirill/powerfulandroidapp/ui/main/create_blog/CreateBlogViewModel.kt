package com.anikinkirill.powerfulandroidapp.ui.main.create_blog

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.anikinkirill.powerfulandroidapp.repository.main.CreateBlogRepository
import com.anikinkirill.powerfulandroidapp.session.SessionManager
import com.anikinkirill.powerfulandroidapp.ui.BaseViewModel
import com.anikinkirill.powerfulandroidapp.ui.DataState
import com.anikinkirill.powerfulandroidapp.ui.Loading
import com.anikinkirill.powerfulandroidapp.ui.main.create_blog.state.CreateBlogStateEvent
import com.anikinkirill.powerfulandroidapp.ui.main.create_blog.state.CreateBlogStateEvent.CreateNewBlogEvent
import com.anikinkirill.powerfulandroidapp.ui.main.create_blog.state.CreateBlogStateEvent.None
import com.anikinkirill.powerfulandroidapp.ui.main.create_blog.state.CreateBlogViewState
import com.anikinkirill.powerfulandroidapp.ui.main.create_blog.state.CreateBlogViewState.NewBlogFields
import com.anikinkirill.powerfulandroidapp.util.AbsentLiveData
import javax.inject.Inject

class CreateBlogViewModel
@Inject constructor(
    val repository: CreateBlogRepository,
    val sessionManager: SessionManager
) : BaseViewModel<CreateBlogStateEvent, CreateBlogViewState>() {
    override fun initNewViewState(): CreateBlogViewState {
        return CreateBlogViewState()
    }

    override fun handleStateEvent(event: CreateBlogStateEvent): LiveData<DataState<CreateBlogViewState>> {
        return when (event) {
            is CreateNewBlogEvent -> {
                AbsentLiveData.create()
            }
            is None -> {
                liveData {
                    emit(
                        DataState(
                            error = null,
                            loading = Loading(true),
                            data = null
                        )
                    )
                }
            }
        }
    }

    fun setNewBlogFields(title: String?, body: String?, uri: Uri?) {
        val update = getCurrentViewStateOrNew()
        val newBlogFields = update.blogFields
        title?.let { newBlogFields.newBlogTitle = it }
        body?.let { newBlogFields.newBlogBody = it }
        uri?.let { newBlogFields.newBlogImage = it }
        update.blogFields = newBlogFields
        setViewState(update)
    }

    fun clearNewBlogFields() {
        val update = getCurrentViewStateOrNew()
        update.blogFields = NewBlogFields()
        setViewState(update)
    }

    fun cancelActiveJobs() {
        repository.cancelActiveJobs()
        handlePendingData()
    }

    private fun handlePendingData() {
        setStateEvent(None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}
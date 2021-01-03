package com.anikinkirill.powerfulandroidapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<StateEvent, ViewState> : ViewModel() {

    private val _stateEvent = MutableLiveData<StateEvent>()
    protected val _viewState = MutableLiveData<ViewState>()

    val viewState get() = _viewState

    val dataState: LiveData<DataState<ViewState>> = Transformations.switchMap(_stateEvent) { stateEvent ->
        stateEvent?.let {
            handleStateEvent(it)
        }
    }

    fun getCurrentViewStateOrNew() : ViewState {
        return viewState.value?.let {
            it
        } ?: initNewViewState()
    }

    abstract fun initNewViewState() : ViewState

    abstract fun handleStateEvent(event: StateEvent) : LiveData<DataState<ViewState>>

    fun setStateEvent(event: StateEvent) {
        _stateEvent.value = event
    }

    fun setViewState(viewState: ViewState) {
        _viewState.value = viewState
    }
}
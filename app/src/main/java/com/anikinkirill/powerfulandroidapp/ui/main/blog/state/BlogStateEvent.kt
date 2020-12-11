package com.anikinkirill.powerfulandroidapp.ui.main.blog.state

sealed class BlogStateEvent {

    class BlogSearchEvent : BlogStateEvent()

    class None : BlogStateEvent()

}
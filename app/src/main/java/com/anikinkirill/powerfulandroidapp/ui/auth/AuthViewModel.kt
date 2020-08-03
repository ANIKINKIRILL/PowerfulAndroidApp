package com.anikinkirill.powerfulandroidapp.ui.auth

import androidx.lifecycle.ViewModel
import com.anikinkirill.powerfulandroidapp.repository.auth.AuthRepository
import javax.inject.Inject

class AuthViewModel @Inject constructor(val authRepository: AuthRepository) : ViewModel() {

}
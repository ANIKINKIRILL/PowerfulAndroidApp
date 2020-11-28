package com.anikinkirill.powerfulandroidapp.ui.main.account

import androidx.lifecycle.LiveData
import com.anikinkirill.powerfulandroidapp.models.AccountProperties
import com.anikinkirill.powerfulandroidapp.repository.main.AccountRepository
import com.anikinkirill.powerfulandroidapp.session.SessionManager
import com.anikinkirill.powerfulandroidapp.ui.BaseViewModel
import com.anikinkirill.powerfulandroidapp.ui.DataState
import com.anikinkirill.powerfulandroidapp.ui.main.account.state.AccountStateEvent
import com.anikinkirill.powerfulandroidapp.ui.main.account.state.AccountStateEvent.*
import com.anikinkirill.powerfulandroidapp.ui.main.account.state.AccountViewState
import com.anikinkirill.powerfulandroidapp.util.AbsentLiveData
import javax.inject.Inject

class AccountViewModel
@Inject constructor(
    private val sessionManager: SessionManager,
    private val accountRepository: AccountRepository
): BaseViewModel<AccountStateEvent, AccountViewState>() {

    override fun initNewViewState(): AccountViewState {
        return AccountViewState()
    }

    override fun handleStateEvent(event: AccountStateEvent): LiveData<DataState<AccountViewState>> {
        when(event) {
            is GetAccountPropertiesEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    accountRepository.getAccountProperties(authToken)
                } ?: AbsentLiveData.create()
            }
            is ChangePasswordEvent -> {
                return AbsentLiveData.create()
            }
            is UpdateAccountPropertiesEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    authToken.account_pk?.let { pk ->
                        accountRepository.updateAccountProperties(authToken, AccountProperties(pk, event.email, event.username))
                    }
                } ?: AbsentLiveData.create()
            }
            is None -> {
                return AbsentLiveData.create()
            }
        }
    }

    fun setAccountProperties(accountProperties: AccountProperties) {
        val update = getCurrentViewStateOrNew()
        if(update.accountProperties == accountProperties) {
            return
        }
        update.accountProperties = accountProperties
        _viewState.value = update
    }

    fun logout() {
        sessionManager.logout()
    }


}
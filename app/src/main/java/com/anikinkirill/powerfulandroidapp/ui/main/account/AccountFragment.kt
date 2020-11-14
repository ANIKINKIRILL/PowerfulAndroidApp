package com.anikinkirill.powerfulandroidapp.ui.main.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.models.AccountProperties
import com.anikinkirill.powerfulandroidapp.ui.main.account.state.AccountStateEvent.GetAccountPropertiesEvent
import kotlinx.android.synthetic.main.fragment_account.*

@SuppressLint("LongLogTag")
class AccountFragment : BaseAccountFragment() {

    companion object {
        const val TAG = "AppDebug_AccountFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        change_password.setOnClickListener {
            navigateToChangePasswordFragment()
        }

        logout_button.setOnClickListener {
            viewModel.logout()
        }

        subscribeObservers()

    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            onDataStateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let { viewState ->
                        viewState.accountProperties?.let { accountProperties ->
                            Log.d(TAG, "AccountFragment, DataState: $accountProperties")
                            viewModel.setAccountProperties(accountProperties)
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.let {
                it.accountProperties?.let { accountProperties ->
                    Log.d(TAG, "AccountFragment, ViewState: $accountProperties")
                    setAccountPropertiesToUi(accountProperties)
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(GetAccountPropertiesEvent())
    }

    private fun setAccountPropertiesToUi(accountProperties: AccountProperties) {
        email.text = accountProperties.email
        username.text = accountProperties.username
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_view_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.edit -> {
                navigateToUpdateAccountFragment()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToChangePasswordFragment() {
        findNavController().navigate(R.id.action_accountFragment_to_changePasswordFragment)
    }

    private fun navigateToUpdateAccountFragment() {
        findNavController().navigate(R.id.action_accountFragment_to_updateAccountFragment)
    }

}
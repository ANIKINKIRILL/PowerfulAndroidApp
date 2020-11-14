package com.anikinkirill.powerfulandroidapp.ui.main.account

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.ui.main.account.state.AccountStateEvent.GetAccountPropertiesEvent
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : BaseAccountFragment() {

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

    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(GetAccountPropertiesEvent())
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
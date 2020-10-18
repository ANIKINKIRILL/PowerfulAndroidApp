package com.anikinkirill.powerfulandroidapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.ui.BaseActivity
import com.anikinkirill.powerfulandroidapp.ui.auth.AuthActivity
import com.anikinkirill.powerfulandroidapp.ui.main.account.ChangePasswordFragment
import com.anikinkirill.powerfulandroidapp.ui.main.account.UpdateAccountFragment
import com.anikinkirill.powerfulandroidapp.ui.main.blog.UpdateBlogFragment
import com.anikinkirill.powerfulandroidapp.ui.main.blog.ViewBlogFragment
import com.anikinkirill.powerfulandroidapp.util.BottomNavController
import com.anikinkirill.powerfulandroidapp.util.BottomNavController.*
import com.anikinkirill.powerfulandroidapp.util.setUpNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), NavGraphProvider, OnNavigationGraphChanged, OnNavigationReselectedListener {

    private lateinit var bottomNavigationView: BottomNavigationView
    private val bottomNavController: BottomNavController by lazy(LazyThreadSafetyMode.NONE) {
        BottomNavController(this, R.id.main_nav_host_fragment, R.id.menu_nav_blog, this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()

        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setUpNavigation(bottomNavController, this)
        if(savedInstanceState == null) {
            bottomNavController.onNavigationItemSelected()
        }

        tool_bar.setOnClickListener {
            sessionManager.logout()
        }

        subscribeObservers()
    }

    private fun subscribeObservers() {
        sessionManager.cachedToken.observe(this, Observer { authToken ->
            Log.d(TAG, "subscribeObservers: $authToken")
            if(authToken == null || authToken.account_pk == -1 || authToken.token == null) {
                navAuthActivity()
            }
        })
    }

    private fun navAuthActivity() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    override fun displayProgressBar(isLoading: Boolean) {
        progress_bar.visibility = if(isLoading) View.VISIBLE else View.INVISIBLE
    }

    override fun getNavGraphId(itemId: Int): Int {
        return when(itemId) {
            R.id.nav_blog -> R.navigation.nav_blog
            R.id.nav_create_blog -> R.navigation.nav_create_blog
            R.id.nav_account -> R.navigation.nav_account
            else -> R.navigation.nav_blog
        }
    }

    override fun onGraphChange() {
        // TODO("Not yet implemented")
    }

    override fun onReselectNavItem(navController: NavController, fragment: Fragment) {
        when(fragment) {
            is ViewBlogFragment -> {
                navController.navigate(R.id.action_viewBlogFragment_to_blogFragment)
            }
            is UpdateBlogFragment -> {
                navController.navigate(R.id.action_updateBlogFragment_to_blogFragment)
            }
            is ChangePasswordFragment -> {
                navController.navigate(R.id.action_changePasswordFragment_to_accountFragment)
            }
            is UpdateAccountFragment -> {
                navController.navigate(R.id.action_updateAccountFragment_to_accountFragment)
            }
            else -> {
                // do nothing
            }
        }
    }

    override fun onBackPressed() = bottomNavController.onBackPressed()

    private fun setupActionBar() {
        setSupportActionBar(tool_bar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}
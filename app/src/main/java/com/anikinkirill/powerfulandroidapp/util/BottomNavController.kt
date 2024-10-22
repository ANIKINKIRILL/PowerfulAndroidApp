package com.anikinkirill.powerfulandroidapp.util

import android.app.Activity
import android.content.Context
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.anikinkirill.powerfulandroidapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Custom bottom navigation controller
 *
 * Helps to easily manage the back stack of fragments
 * The core concept took from here:
 * https://stackoverflow.com/questions/50577356/android-jetpack-navigation-bottomnavigationview-with-youtube-or-instagram-like#_=_
 */

class BottomNavController(
    private val context: Context,
    @IdRes val containerId: Int,            // R.id.main_nav_host_fragment
    @IdRes private val appStartDestinationId: Int,
    private val graphChangeListener: OnNavigationGraphChanged?,
    private val navGraphProvider: NavGraphProvider
) {

    companion object {
        private const val TAG = "AppDebug_BottomNavController"
    }

    lateinit var activity: Activity
    lateinit var fragmentManager: FragmentManager
    lateinit var navItemChangeListener: OnNavigationItemChanged
    private val navigationBackStack = BackStack.of(appStartDestinationId)


    init {
        if(context is Activity) {
            activity = context
            fragmentManager = (activity as FragmentActivity).supportFragmentManager
        }
    }

    fun onNavigationItemSelected(itemId: Int = navigationBackStack.last()) : Boolean {
        // Find a fragment or create a new one
        val fragment = fragmentManager.findFragmentByTag(itemId.toString()) ?: NavHostFragment.create(navGraphProvider.getNavGraphId(itemId))

        // Replace the fragment representing a navigation item
        fragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
            )
            .replace(containerId, fragment, itemId.toString())
            .addToBackStack(null)
            .commit()

        // Add to back stack
        navigationBackStack.moveLast(itemId)

        // Update checked icon
        navItemChangeListener.onItemChange(itemId)

        // Communicate with Activity
        graphChangeListener?.onGraphChange()

        return true
    }

    /**
     *  For highlighting the selected item
     *  in the BottomNavigationMenu
     *  Example: from HOME user clicks CREATE
     *  So CREATE item is going to be highlighted (blue color)
     *
     */
    interface OnNavigationItemChanged {
        /**
         * @param itemId - R.menu.menu_nav_blog, R.menu.menu_nav_create_blog, R.menu.menu_nav_account
         */
        fun onItemChange(itemId: Int)
    }

    fun setOnItemNavigationChanged(listener: (itemId: Int) -> Unit) {
        navItemChangeListener = object : OnNavigationItemChanged {
            override fun onItemChange(itemId: Int) {
                listener.invoke(itemId)
            }
        }
    }

    /**
     * Get navigation graph id
     * @NavigationRes means that returned type of
     * getNavGraphId is going to be navigation resource reference
     * Example: R.navigation.nav_blog
     */

    interface NavGraphProvider {
        /**
         * @param itemId - R.menu.menu_nav_blog, R.menu.menu_nav_create_blog, R.menu.menu_nav_account
         * @return navigation resource reference (e.g R.navigation.nav_blog)
         */
        @NavigationRes
        fun getNavGraphId(itemId: Int) : Int
    }

    /**
     *  Tell the Activity that graph is changed now
     *  So when user goes from HOME to ACCOUNT
     *  this interface in going to be triggered
     *
     *  DO NOT MISUNDERSTAND with [OnNavigationItemChanged] interface,
     *  because [OnNavigationItemChanged] is used INTERNALLY
     *  only in [BottomNavController] class
     *
     *  But [OnNavigationGraphChanged] is used for telling the
     *  activity that graph is different
     */

    interface OnNavigationGraphChanged {
        fun onGraphChange()
    }

    /**
     * Double click on already selected item
     * will pop all the stack to the root, refreshing it
     * Example: Home -> BlogFragment -> ViewBlogFragment -> UpdateBlogFragment
     * double click on HOME will reselect everything and go to HOME root fragment
     */

    interface OnNavigationReselectedListener {
        fun onReselectNavItem(navController: NavController, fragment: Fragment)
    }

    private class BackStack : ArrayList<Int>() {

        companion object {
            fun of(vararg elements: Int) : BackStack {
                val backStack = BackStack()
                backStack.addAll(elements.toTypedArray())
                return backStack
            }
        }

        fun removeLast() = removeAt(size - 1)
        fun moveLast(item: Int) {
            remove(item)
            add(item)
        }
    }

    fun onBackPressed() {
        val childFragmentManager = fragmentManager.findFragmentById(containerId)!!.childFragmentManager
        when {
            childFragmentManager.popBackStackImmediate() -> {

            }

            // Fragment back stack is empty so try to go back on the navigation stack
            navigationBackStack.size > 1 -> {
                // Remove last element from navigation back stack
                navigationBackStack.removeLast()

                // Update the container with a new fragment
                onNavigationItemSelected()
            }

            // If the stack has only one and it's not the navigation home we should
            // ensure that the application always leave from startDestination
            navigationBackStack.last() != appStartDestinationId -> {
                navigationBackStack.removeLast()
                navigationBackStack.add(0, appStartDestinationId)
                onNavigationItemSelected()
            }

            // Navigation stack is empty, so finish the activity
            else -> activity.finish()

        }
    }

}

fun BottomNavigationView.setUpNavigation(
    bottomNavController: BottomNavController,
    onReselectListener: BottomNavController.OnNavigationReselectedListener
) {

    // Get this function from BottomNavigationView
    // Set a listener that will be notified when a bottom navigation item is selected
    setOnNavigationItemSelectedListener {
        bottomNavController.onNavigationItemSelected(it.itemId)
    }

    // Get this function from BottomNavigationView
    // Set a listener that will be notified when the currently selected bottom navigation item is reselected
    // When item graph (HOME, CREATE, ACCOUNT) is already selected, but user select this item one more time
    // Every child/detail fragments should be gone. So user see the very first fragment
    setOnNavigationItemReselectedListener {
        bottomNavController
            .fragmentManager
            .findFragmentById(bottomNavController.containerId)!!
            .childFragmentManager
            .fragments[0]?.let { fragment ->
            onReselectListener.onReselectNavItem(
                bottomNavController.activity.findNavController(bottomNavController.containerId),
                fragment
            )
        }
    }

    bottomNavController.setOnItemNavigationChanged { itemId ->
        menu.findItem(itemId).isChecked = true
    }

}
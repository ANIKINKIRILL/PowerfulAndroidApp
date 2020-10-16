package com.anikinkirill.powerfulandroidapp.util

import android.app.Activity
import android.content.Context
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController

class BottomNavController(
    private val context: Context,
    @IdRes private val containerId: Int,            // R.id.main_nav_host_fragment
    @IdRes private val appStartDestinationId: Int,
    private val onNavigationGraphChanged: OnNavigationGraphChanged,
    private val navGraphProvider: NavGraphProvider
) {

    companion object {
        private const val TAG = "AppDebug_BottomNavController"
    }

    lateinit var activity: Activity
    lateinit var fragmentManager: FragmentManager
    lateinit var navItemChangeListener: OnNavigationItemChanged

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

    private fun setOnItemNavigationChanged(listener: (itemId: Int) -> Unit) {
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

}
package com.anikinkirill.powerfulandroidapp.util

import androidx.annotation.NavigationRes

class BottomNavController() {

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

}
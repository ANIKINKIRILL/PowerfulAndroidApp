package com.anikinkirill.powerfulandroidapp.util

class BottomNavController() {

    /**
     *  For highlighting the selected item
     *  in the BottomNavigationMenu
     *  Example: from HOME user clicks CREATE
     *  So CREATE item is going to be highlighted (blue color)
     *
     */
    interface OnNavigationItemChanged {
        // itemId - R.menu.menu_nav_blog, R.menu.menu_nav_create_blog, R.menu.menu_nav_account
        fun onItemChange(itemId: Int)
    }

}
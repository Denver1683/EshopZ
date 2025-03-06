package com.example.eshopz

sealed class NavigationController(val route: String){
    object LoginPage: NavigationController(route = "login_page")
    object HomePage: NavigationController(route = "home_page")
    object ProfilePage: NavigationController(route = "profile_page")
    object ProductCard: NavigationController(route = "product_card")
    object AddItemPage: NavigationController(route = "additem_page")
    object ProductPage: NavigationController(route = "product_page")
    object SellerPage: NavigationController(route = "seller_page")
    object CartPage: NavigationController(route = "cart_page")
    object ProductDetails: NavigationController(route = "product_details")
    object WishlistPage: NavigationController(route= "wishlist_page")
    object PurchasedPage: NavigationController(route= "purchased_page")
    //object CartCard: NavigationController(route= "cart_card")
    fun withArgs(vararg args: String?): String{
        return buildString {
            append(route)
            args.forEach { arg->
                append("/$arg")
            }
        }
    }
}

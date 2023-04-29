package com.sanryoo.matcher.features.util

sealed class Screen(val route: String) {
    //Nav Root
    object OptionLogin : Screen(route = "option_log_in_screen")
    object LogIn : Screen(route = "log_in_screen")
    object SignUp : Screen(route = "sign_up_screen")
    object Using : Screen(route = "using_screen")

    //Nav Using
    object Home : Screen(route = "home_screen")
    object Profile : Screen(route = "profile_screen")
    object Menu : Screen(route = "menu_screen")
    object Password : Screen(route = "password_screen")
    object Message : Screen(route = "message_screen")
    object OtherInformation : Screen(route = "others_screen")
    object AboutScreen : Screen(route = "about_screen")
}

object BottomSheet {
    const val DEFAULT = 0
    const val SEX = 1
    const val STATUS = 2
    const val AGE = 3
    const val HEIGHT = 4
    const val WEIGHT = 5
    const val INCOME = 6
    const val DISTANCE = 7
    const val APPEARANCE = 8
}

object MessageType {
    const val TEXT = 0
    const val IMAGE = 1
}

object MyString {
    //For User
    const val USER = "user"
    const val OTHERS = "others"

    //For Int
    const val SIGN_UP = "sign_up"
    const val SENDING_MESSAGE = "sending_message"

    //Connectivity
    const val AVAILABLE = "available"
    const val NOT_AVAILABLE = "not_available"

}
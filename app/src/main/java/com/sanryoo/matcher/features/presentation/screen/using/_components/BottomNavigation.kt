package com.sanryoo.matcher.features.presentation.screen.using._components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.presentation.screen.using.BottomNavItem
import com.sanryoo.matcher.features.util.Screen

@Composable
fun CustomBottomNavigation(
    navController: NavHostController
) {
    val context = LocalContext.current
    val listItem = listOf(
        BottomNavItem(
            context.getString(R.string.home),
            Screen.Home.route,
            R.drawable.home_selected,
            R.drawable.home_unselected
        ), BottomNavItem(
            context.getString(R.string.profile),
            Screen.Profile.route,
            R.drawable.person_selected,
            R.drawable.person_unselected
        ), BottomNavItem(
            context.getString(R.string.menu),
            Screen.Menu.route,
            R.drawable.menu_selected,
            R.drawable.menu_unselected
        )
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = listItem.any { it.route == currentDestination?.route }

    if (showBottomBar) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listItem.forEach { item ->
                CustomItem(modifier = Modifier.weight(1f),
                    item = item,
                    navController = navController,
                    onClick = {
                        if (navController.currentBackStackEntry?.destination?.route != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(Screen.Home.route)
                                launchSingleTop = true
                            }
                        }
                    })
            }
        }
    }
}

@Composable
fun CustomItem(
    modifier: Modifier = Modifier,
    item: BottomNavItem,
    navController: NavHostController,
    onClick: () -> Unit = {},
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val selected = item.route == backStackEntry.value?.destination?.route
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colors.background)
            .clickable(
                onClick = onClick, interactionSource = MutableInteractionSource(), indication = null
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = if (selected) item.iconSelected else item.iconUnSelected),
                contentDescription = item.title,
            )
            AnimatedVisibility(visible = selected) {
                Text(
                    text = item.title,
                    fontSize = 12.sp,
                )
            }
        }
    }
}
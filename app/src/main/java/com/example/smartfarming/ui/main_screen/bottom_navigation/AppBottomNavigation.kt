package com.example.smartfarming.ui.main_screen.bottom_navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartfarming.ui.addactivities.ui.theme.MainGreen

@Composable
fun AppBottomNavigation(
    navController: NavController,
    setCurrentRoute : (String?) -> Unit
) {
    val navItem = listOf(NavItem.Home, NavItem.Garden, NavItem.Harvest, NavItem.Profile)
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    BottomNavigation(
        backgroundColor = Color.White,
        modifier = Modifier
            .height(90.dp)
    ) {
        setCurrentRoute(currentRoute)
            navItem.forEach { item ->
                BottomNavigationItem(
                    modifier = Modifier.padding(bottom = 15.dp),
                    icon = {
                        Icon(
                            item.icon,
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .padding(bottom = 3.dp)
                        )
                           },
                    alwaysShowLabel = false,
                    label = {
                        Text(
                            text = stringResource(id = item.title),
                            style = MaterialTheme.typography.subtitle2
                        )
                            },
                    selected = currentRoute == item.navRoute ,
                    selectedContentColor = MainGreen,
                    unselectedContentColor = MainGreen.copy(0.6f),
                    onClick = {
                        navController.navigate(item.navRoute) {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
    }
}

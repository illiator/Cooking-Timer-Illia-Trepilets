package ua.opnu.compapp.ui.components.navbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material3.*
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

@Composable
fun NavBar(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar() {
        NavBarItem.screens.forEach {item ->
            this.AddItem(screen = item, currentDestination = currentDestination, navController = navController)
        }
    }
}



@Composable
fun RowScope.AddItem(
    screen: NavBarItem,
    currentDestination: NavDestination?,
    navController: NavController
) {
    NavigationBarItem(
        label = { Text(screen.title) },
        icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = { navController.navigate(screen.route) }
    )
}
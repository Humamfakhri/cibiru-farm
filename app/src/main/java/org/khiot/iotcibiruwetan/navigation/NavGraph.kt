package org.khiot.iotcibiruwetan.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.khiot.iotcibiruwetan.ui.screen.hidroponik.HidroponikScreen
import org.khiot.iotcibiruwetan.ui.screen.kebuncabe.KebunCabeScreen
import org.khiot.iotcibiruwetan.ui.screen.kolam.KolamScreen
import org.khiot.iotcibiruwetan.ui.theme.PlusJakartaSans

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object KebunCabe : Screen("kebunCabe", "Kebun Cabe", Icons.Default.Home)
    object Hidroponik : Screen("hidroponik", "Hidroponik", Icons.Default.Home)
    object Kolam : Screen("kolam", "Kolam", Icons.Default.Home)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
//    val items = listOf(
//        BottomNavItem.Home,
//        BottomNavItem.Search,
//        BottomNavItem.Profile
//    )

    val items = listOf(
        Screen.KebunCabe,
        Screen.Hidroponik,
        Screen.Kolam
    )

    Scaffold(
        bottomBar = {
//            NavigationBar() {
            NavigationBar(containerColor = Color(255, 255, 255)) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title, fontFamily = PlusJakartaSans, fontWeight = FontWeight.Bold) },
                        selected = currentDestination?.route == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.KebunCabe.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.KebunCabe.route) { KebunCabeScreen() }
            composable(Screen.Hidroponik.route) { HidroponikScreen() }
            composable(Screen.Kolam.route) { KolamScreen() }
        }
    }
}

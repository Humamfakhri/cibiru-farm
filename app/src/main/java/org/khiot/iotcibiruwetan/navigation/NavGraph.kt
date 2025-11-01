package org.khiot.iotcibiruwetan.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.khiot.iotcibiruwetan.ui.screen.hidroponik.HidroponikScreen
import org.khiot.iotcibiruwetan.ui.screen.kebuncabe.KebunCabeScreen
import org.khiot.iotcibiruwetan.ui.screen.kolam.KolamScreen
import org.khiot.iotcibiruwetan.ui.theme.PlusJakartaSans

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object KebunCabe : Screen("kebunCabe", "Kebun Cabe", Icons.Default.Home)
    object Hidroponik : Screen("hidroponik", "Hidroponik", Icons.Default.Home)
    object Kolam : Screen("kolam", "Kolam", Icons.Default.Home)
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var selectedDrawerIndex by rememberSaveable { mutableIntStateOf(0) }

    val drawerScreens = listOf(
        Screen.KebunCabe,
        Screen.Hidroponik,
        Screen.Kolam
    )

    // Scaffold utama dengan sidebar
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { scope.launch { drawerState.close() } }) {
                        Icon(Icons.Default.Close, contentDescription = "Close drawer")
                    }
//                    Text("Menu", style = MaterialTheme.typography.titleMedium)
                }

                // Daftar menu sidebar
                drawerScreens.forEachIndexed { index, screen ->
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = screen.title,
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        selected = selectedDrawerIndex == index,
                        onClick = {
                            selectedDrawerIndex = index
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            drawerScreens[selectedDrawerIndex].title,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Open Menu"
                            )
                        }
                    }
                )
            },
            contentWindowInsets = WindowInsets(0) // supaya tidak ada jarak ekstra
        ) { innerPadding ->
            Box(
                Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
                    .fillMaxSize()
            ) {
                when (selectedDrawerIndex) {
                    0 -> KebunCabeContainer()
                    1 -> HidroponikScreen()
                    2 -> KolamScreen()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun KebunCabeContainer() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("Kebun Cabe", "Riwayat")

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = title) },
                        label = { Text(title, fontWeight = FontWeight.Bold) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color.White,
//                            unselectedIconColor = Color(0xFFE0F7FA),
//                            unselectedTextColor = Color(0xFFE0F7FA),
                            indicatorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            if (selectedTab == 0) KebunCabeScreen()
            else RiwayatScreen()
        }
    }
}

@Composable
fun RiwayatScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Riwayat Data", fontWeight = FontWeight.Bold)
    }
}

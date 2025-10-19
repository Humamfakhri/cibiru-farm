package org.khiot.iotcibiruwetan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import org.khiot.iotcibiruwetan.navigation.MainScreen
import org.khiot.iotcibiruwetan.ui.theme.MyAppTheme

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MyAppTheme {
//                MainScreen()
//            }
//        }
//    }
//}

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            // Atur warna status bar
//            val systemUiController = rememberSystemUiController()
//            systemUiController.setNavigationBarColor(Color.White, darkIcons = true)
//            SideEffect {
//                systemUiController.setSystemBarsColor(
//                    color = Color.White,   // warna notif bar
//                    darkIcons = true       // ikon hitam
//                )
//            }
//
//            // Bungkus dengan MaterialTheme
//            MaterialTheme(
//                colorScheme = androidx.compose.material3.lightColorScheme(
//                    primary = Color(0xFF0EB1D2) // warna primary custom
//                )
//            ) {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    // Panggil Composable utama
//                    MainScreen()
//                }
//            }
//        }
//    }
//}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Aktifkan edge-to-edge dan atur status bar jadi putih dengan ikon gelap
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,  // warna status bar
                android.graphics.Color.BLACK   // warna ikon (darkIcons)
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.WHITE,
                android.graphics.Color.BLACK
            )
        )

        // Supaya konten tidak "terpotong", biarkan insets dipakai
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}
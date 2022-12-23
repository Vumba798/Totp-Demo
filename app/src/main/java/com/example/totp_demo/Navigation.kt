package com.example.totp_demo

import android.content.SharedPreferences
import android.view.View
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.totp_demo.ui.screens.LockScreen
import com.example.totp_demo.ui.screens.MainScreen
import java.util.prefs.Preferences

@Composable
fun MyNavHost(navController: NavHostController, prefs: SharedPreferences, isUnlocked: Boolean) {
    var home = if (isUnlocked) "main" else "lock"
    NavHost(navController, home) {
        composable("main") { MainScreen(navController, prefs) }
//        composable("qr_scanner") { QrScannerScreen(root, value) }
        composable("lock") { LockScreen(navController, prefs) }
    }
}
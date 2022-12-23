package com.example.totp_demo.ui.screens

import android.content.SharedPreferences
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.totp_demo.Totp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import java.nio.ByteBuffer
import java.util.prefs.Preferences

@Composable
fun MainScreen(navController: NavHostController, prefs: SharedPreferences) {
    var scannedData by rememberSaveable { mutableStateOf("") }
    var rawKey by remember { mutableStateOf(prefs.getString("rawKey", "")!!) }
    var code by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
        onResult = { result ->
            Toast.makeText(context, result.contents, Toast.LENGTH_LONG).show()
            scannedData = result.contents
            rawKey = parseScannedData(result.contents)
            val editor = prefs.edit()
            editor.putString("rawKey", rawKey)
            editor.apply()
        }
    )
    Box {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    scanLauncher.launch(ScanOptions())
                }
            ) {
                Text("Scan")
            }
            Button(
                onClick = {
                    if (rawKey.length == 32) {
                        code = Totp.oneTimePassword(
                            //rawKey = "eo4o pfj2 nlzc brza cigt y2ca 5ukt bbrh",
                            rawKey = rawKey,
                            rawValue = (System.currentTimeMillis() / 1000L / 30)
                        )
                    }
                }
            ) {
                Text("Refresh")
            }
            //Text("scanned: ${scannedData} ")
            Text("code:\n$code", fontSize = 30.sp)
            //Text("rawKey: ${rawKey} ")
        }
    }
}

fun parseScannedData(scannedData: String): String {
    val begin = scannedData.indexOf("?secret", 0)
    val end = begin + 40
    return if (begin == -1) {
        ""
    } else {
        scannedData.substring((begin + 8) until end)
    }
}
/*
@Composable
fun MainScreen() {
    var code by remember { mutableStateOf("")}
    Box {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                 onClick = {
                     code = Totp.oneTimePassword(
                         rawKey = "qqzp fvub sbsb y6th jpoq nkeb x53y 2kbh",
                         rawValue = (System.currentTimeMillis() / 1000L / 30)
                     )
                 }
            ) {
                Text("Refresh")
            }
            Text("Code: $code")
        }
    }
}
 */
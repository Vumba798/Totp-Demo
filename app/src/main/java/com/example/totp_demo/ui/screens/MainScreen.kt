package com.example.totp_demo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.totp_demo.Totp
import java.nio.ByteBuffer

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
                         rawKey = "qaol pfg2 gxuh nf73 gdkr lfgt 62tq hr4h",
                         rawValue = (System.currentTimeMillis() / 1000L / 30L)
                     )
                 }
            ) {
                Text("Refresh")
            }
            Text("Code: $code")
        }
    }
}


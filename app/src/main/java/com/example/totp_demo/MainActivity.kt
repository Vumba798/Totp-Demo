package com.example.totp_demo

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.rememberNavController
import com.google.zxing.integration.android.IntentIntegrator


class MainActivity: ComponentActivity() {
    private val text = MutableLiveData("")
    private val zxingActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val intentResult = IntentIntegrator.parseActivityResult(it.resultCode, it.data)
        if(intentResult.contents != null) {
            Toast.makeText(this, intentResult.contents, Toast.LENGTH_LONG).show()
        }
    }
    lateinit var prefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getPreferences(android.content.Context.MODE_PRIVATE)
        setContent {
            TotpApp(prefs)
        }
    }

}

@Composable
fun TotpApp(prefs: SharedPreferences) {
    var isUnlocked by remember { mutableStateOf(false) }
    val navController = rememberNavController()
    MyNavHost(navController, prefs, isUnlocked)
}

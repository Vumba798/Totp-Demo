package com.example.totp_demo.ui.screens
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.util.prefs.Preferences


@Composable
fun LockScreen(
    navController: NavHostController,
    prefs: SharedPreferences,
    modifier: Modifier = Modifier,
) {
    var pin by rememberSaveable { mutableStateOf("") }
    var pinButtonEnabled by rememberSaveable { mutableStateOf(false) }
    var pinError by rememberSaveable { mutableStateOf(false) }
    val mContext = LocalContext.current


    val MarginQuad = 8.dp
    val MarginDouble = 4.dp
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = com.example.totp_demo.R.string.app_name),
                modifier = Modifier.padding(horizontal = MarginQuad),
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(MarginDouble))
            if (pin == "") {
                Text("Please, input your pin")
            }
            PasswordTextField(
                value = pin,
                onValueChange = {
                    pin = it
                    pinError = false
                    pinButtonEnabled = !(pin.length < 4 || pin.length > 32)
                },
                label = { Text(text = stringResource(id = com.example.totp_demo.R.string.pin)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                isError = pinError,
            )
            Spacer(modifier = Modifier.height(MarginQuad))
            Button(
                onClick = {
                    if (checkPin(pin, prefs)) {
                        navController.navigate("main")
                    } else if (getPinHash(prefs) == "EMPTY".hashCode()) {
                        val editor = prefs.edit()
                        editor.putInt("pin", pin.hashCode())
                        editor.apply()
                        Toast.makeText(mContext, "Pin has been successfully saved!", Toast.LENGTH_SHORT)
                    } else {
                        pinError = true
                        pin = ""
                    }
                },
                enabled = pinButtonEnabled,
                modifier = Modifier
                    .padding(end = 70.dp)
                    .align(Alignment.End)
            ) {
                Text(
                    "Okay"
                )
            }
        }
    }
}


@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    label: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors()
) {
    // 1
    var passwordVisible: Boolean by rememberSaveable {
        mutableStateOf(false)
    }

    // 2
    PasswordTextField(
        value = value,
        onValueChange = onValueChange,
        passwordVisible = passwordVisible,
        onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
        modifier = modifier,
        enabled = enabled,
        isError = isError,
        keyboardOptions = keyboardOptions,
        label = label,
        colors = colors,
    )
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    label: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors()
) {
    // 3
    OutlinedTextField(
        value = value,
        label = label,
        enabled = enabled,
        isError = isError,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(
                onClick = onTogglePasswordVisibility,
            ) {
                Crossfade(
                    targetState = passwordVisible,
                ) { visible ->
                    Icon(
                        painter = painterResource(
                            id = if (visible) {
                                com.example.totp_demo.R.drawable.ic_baseline_visibility_24
                            } else {
                                com.example.totp_demo.R.drawable.ic_baseline_visibility_off_24
                            }
                        ),
                        contentDescription = stringResource(com.example.totp_demo.R.string.content_desc_toggle_password_visibility),
                    )
                }
            }
        },
        onValueChange = onValueChange,
        modifier = modifier,
        colors = colors,
    )
}

fun getPinHash(prefs: SharedPreferences) =
    prefs.getInt("pin", "EMPTY".hashCode())

fun checkPin(pin: String, prefs: SharedPreferences): Boolean =
    getPinHash(prefs) == pin.hashCode()

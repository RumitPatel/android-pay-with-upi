package com.rum.upipaymentapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.rum.upipaymentapp.ui.custom_views.MyPasswordTextField
import com.rum.upipaymentapp.ui.custom_views.MyText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    onBackArrowPressed: () -> Unit = {},
    onResetButtonClicked: (currentPassword: String?, newPassword: String?, confirmNewPassword: String?) -> Unit = { _: String?, _: String?, _: String? -> },
) {

    var currentPassword by rememberSaveable { mutableStateOf("") }
    var currentPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isErrorCurrentPassword by rememberSaveable { mutableStateOf(false) }

    var newPassword by rememberSaveable { mutableStateOf("") }
    var newPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isErrorNewPassword by rememberSaveable { mutableStateOf(false) }

    var confirmNewPassword by rememberSaveable { mutableStateOf("") }
    var confirmNewPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isErrorConfirmNewPassword by rememberSaveable { mutableStateOf(false) }


    Scaffold(topBar = {
        TopAppBar(title = {
            MyText(text = stringResource(R.string.app_name))
        }, navigationIcon = {
            IconButton(onClick = onBackArrowPressed) {
                Icon(Icons.Filled.ArrowBack, "")
            }
        },
        )
    }, content = { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
            MyText(
                modifier = Modifier.padding(2.dp),
                text = stringResource(R.string.app_name),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
            MyText(
                modifier = Modifier.padding(12.dp),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.app_name),
                fontSize = 18.sp,
            )

            MyPasswordTextField(value = currentPassword,
                isError = isErrorCurrentPassword,
                errorMessage = stringResource(R.string.app_name),
                valueVisibility = currentPasswordVisible,
                placeholder = stringResource(R.string.app_name),
                label = stringResource(R.string.app_name),
                onValueChange = {
                    currentPassword = it
                    isErrorCurrentPassword = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                ),
                trailingIcon = {
                    val image = getPasswordToggleIcon(currentPasswordVisible)
                    val description = getPasswordToggleDescription(confirmNewPasswordVisible)

                    IconButton(onClick = { currentPasswordVisible = !currentPasswordVisible }) {
                        Icon(imageVector = image, description)
                    }
                })

            MyPasswordTextField(value = newPassword,
                isError = isErrorNewPassword,
                errorMessage = stringResource(R.string.app_name),
                valueVisibility = newPasswordVisible,
                placeholder = stringResource(R.string.app_name),
                label = stringResource(R.string.app_name),
                onValueChange = {
                    newPassword = it
                    isErrorNewPassword = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                ),
                trailingIcon = {
                    val image = getPasswordToggleIcon(newPasswordVisible)
                    val description = getPasswordToggleDescription(confirmNewPasswordVisible)

                    IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                        Icon(imageVector = image, description)
                    }
                })

            MyPasswordTextField(value = confirmNewPassword,
                isError = isErrorConfirmNewPassword,
                errorMessage = stringResource(R.string.app_name),
                valueVisibility = confirmNewPasswordVisible,
                placeholder = stringResource(R.string.app_name),
                label = stringResource(R.string.app_name),
                onValueChange = {
                    confirmNewPassword = it
                    isErrorConfirmNewPassword = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    val image = getPasswordToggleIcon(confirmNewPasswordVisible)
                    val description = getPasswordToggleDescription(confirmNewPasswordVisible)

                    IconButton(onClick = {
                        confirmNewPasswordVisible = !confirmNewPasswordVisible
                    }) {
                        Icon(imageVector = image, description)
                    }
                })

            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    if (currentPassword.isEmpty()) {
                        isErrorCurrentPassword = true
                    } else if (newPassword.isEmpty()) {
                        isErrorNewPassword = true
                    } else if (confirmNewPassword.isEmpty()) {
                        isErrorConfirmNewPassword = true
                    } else {
                        onResetButtonClicked(currentPassword, newPassword, confirmNewPassword)
                    }

                }) {
                MyText(
                    text = stringResource(R.string.app_name).uppercase(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    })
}


fun getPasswordToggleIcon(isVisible: Boolean): ImageVector {
    return if (isVisible) Icons.Filled.Visibility
    else Icons.Filled.VisibilityOff
}

fun getPasswordToggleDescription(isVisible: Boolean): String {
    return if (isVisible) "Hide password" else "Show password"
}
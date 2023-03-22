package com.rum.upipaymentapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rum.upipaymentapp.ui.custom_views.MyText
import com.rum.upipaymentapp.ui.custom_views.MyTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    onPayNowButtonClicked: (amount: String?, upi: String?, name: String?, note: String?) -> Unit = { _: String?, _: String?, _: String?, _: String? -> },
) {

    var amount by rememberSaveable { mutableStateOf("") }
    var isErrorAmount by rememberSaveable { mutableStateOf(false) }

    var upi by rememberSaveable { mutableStateOf("") }
    var isErrorUpi by rememberSaveable { mutableStateOf(false) }

    var name by rememberSaveable { mutableStateOf("") }
    var isErrorName by rememberSaveable { mutableStateOf(false) }

    var note by rememberSaveable { mutableStateOf("") }
    var isErrorNote by rememberSaveable { mutableStateOf(false) }


    Scaffold(topBar = {
        TopAppBar(
            title = {
                MyText(text = stringResource(R.string.app_name))
            },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.Face, "")
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
            Spacer(modifier = Modifier.height(80.dp))
            MyTextField(
                value = amount,
                isError = isErrorAmount,
                errorMessage = stringResource(R.string.err_amount),
                placeholder = stringResource(R.string.amount_place_holder),
                label = stringResource(R.string.amount),
                onValueChange = {
                    amount = it
                    isErrorAmount = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next
                )
            )

            MyTextField(
                value = upi,
                isError = isErrorUpi,
                errorMessage = stringResource(R.string.err_upi),
                placeholder = stringResource(R.string.upi_place_holder),
                label = stringResource(R.string.upi),
                onValueChange = {
                    upi = it
                    isErrorUpi = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                )
            )

            MyTextField(
                value = name,
                isError = isErrorName,
                errorMessage = stringResource(R.string.err_name),
                placeholder = stringResource(R.string.name_place_holder),
                label = stringResource(R.string.name),
                onValueChange = {
                    name = it
                    isErrorName = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                )
            )

            MyTextField(
                value = note,
                isError = isErrorNote,
                errorMessage = stringResource(R.string.err_note),
                placeholder = stringResource(R.string.note_place_holder),
                label = stringResource(R.string.note),
                onValueChange = {
                    note = it
                    isErrorNote = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                )
            )

            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    if (amount.isEmpty()) {
                        isErrorAmount = true
                    } else if (upi.isEmpty()) {
                        isErrorUpi = true
                    } else if (name.isEmpty()) {
                        isErrorName = true
                    } else if (note.isEmpty()) {
                        isErrorNote = true
                    } else {
                        onPayNowButtonClicked(amount, upi, name, note)
                    }

                }) {
                MyText(
                    text = stringResource(R.string.pay_now).uppercase(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    })
}
package com.rum.upipaymentapp.ui.custom_views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.rum.upipaymentapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPasswordTextField(
    value: String,
    valueVisibility: Boolean,
    isError: Boolean,
    errorMessage: String,
    placeholder: String,
    label: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    TextField(
        value = value,
        placeholder = { MyText(text = placeholder) },
        label = { MyText(text = label) },
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        visualTransformation = if (valueVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = trailingIcon,
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent, //hide the bottom indicator line
            unfocusedIndicatorColor = Color.Transparent //hide the bottom indicator line
        )
    )
    if (isError) {
        MyText(
            text = errorMessage,
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp)
        )
    }
}

@Composable
fun MyText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        fontWeight = fontWeight,
        fontSize = fontSize,
        textAlign = textAlign
    )
}
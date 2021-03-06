package com.example.smartfarming.ui.authentication.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.smartfarming.ui.addactivities.ui.theme.MainGreen

@Composable
fun Step1(
    firstName : String,
    lastName: String,
    changeFirstName : (String) -> Unit,
    changeLastName : (String) -> Unit,
    increaseStep : () -> Unit
){

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = firstName,
            onValueChange = {
                changeFirstName(it)
            },
            textStyle = MaterialTheme.typography.body1,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                unfocusedBorderColor = MainGreen,
                focusedLabelColor = MainGreen,
                focusedBorderColor = MainGreen,
                unfocusedLabelColor = MainGreen
            ),
            shape = MaterialTheme.shapes.medium,
            label = {
                Text(
                    text = "نام",
                    style = MaterialTheme.typography.body1
                )
            },
            modifier = Modifier
                .padding(10.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {focusManager.moveFocus(FocusDirection.Down)}
            ),
            trailingIcon = {
                Icon(
                    Icons.Default.VerifiedUser,
                    contentDescription = "",
                    tint = MainGreen
                )
            }
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = {
                changeLastName(it)
            },
            textStyle = MaterialTheme.typography.body1,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                unfocusedBorderColor = MainGreen,
                focusedLabelColor = MainGreen,
                focusedBorderColor = MainGreen,
                unfocusedLabelColor = MainGreen
            ),
            shape = MaterialTheme.shapes.medium,
            label = {
                Text(
                    text = "نام خانوادگی",
                    style = MaterialTheme.typography.body1
                )
            },
            modifier = Modifier
                .padding(10.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            trailingIcon = {
                Icon(Icons.Default.Badge, contentDescription = "", tint = MainGreen)
            }
        )
    }
}
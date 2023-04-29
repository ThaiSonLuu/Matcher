package com.sanryoo.matcher.features.presentation.screen.using._components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanryoo.matcher.R
import com.sanryoo.matcher.ui.theme.Primary

@ExperimentalComposeUiApi
@Composable
fun ChangeName(
    currentName: String = "",
    onCancel: () -> Unit = {},
    onChange: (String) -> Unit = {}
) {
    val context = LocalContext.current
    AlertDialog(
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(20.dp),
        onDismissRequest = onCancel,
        title = null,
        text = null,
        buttons = {
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(key1 = true) {
                focusRequester.requestFocus()
            }
            var tempName by remember {
                mutableStateOf(
                    TextFieldValue(
                        currentName,
                        selection = TextRange(currentName.length)
                    )
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = context.getString(R.string.your_name),
                    fontSize = 20.sp,
                    color = Color.Blue,
                    modifier = Modifier.padding(10.dp)
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 40.dp)
                        .width(180.dp)
                        .height(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colors.background
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Blue,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.CenterStart,

                    ) {
                    BasicTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        textStyle = TextStyle(
                            color = MaterialTheme.colors.onBackground
                        ),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .focusRequester(focusRequester),
                        cursorBrush = SolidColor(Primary),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        ),
                        singleLine = true
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .width(260.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onCancel,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Blue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = context.getString(R.string.cancel))
                    }
                    Button(
                        onClick = { onChange(tempName.text) },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Blue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = context.getString(R.string.change))
                    }
                }
            }
        }
    )
}
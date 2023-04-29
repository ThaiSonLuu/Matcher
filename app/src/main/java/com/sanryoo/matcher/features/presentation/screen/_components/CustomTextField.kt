package com.sanryoo.matcher.features.presentation.screen._components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.sanryoo.matcher.ui.theme.SecondContentDark
import com.sanryoo.matcher.ui.theme.SecondContentLight

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChang: (String) -> Unit = {},
    placeHolder: String = "",
    icon: Int,
    onClickIcon: () -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onClickImeAction: () -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChang,
        shape = RoundedCornerShape(25.dp),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = MaterialTheme.colors.surface,
        ),
        trailingIcon = {
            IconButton(onClick = onClickIcon) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "",
                    modifier = Modifier.size(15.dp),
                    tint = MaterialTheme.colors.onBackground
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions(onDone = { onClickImeAction() }),
        visualTransformation = visualTransformation,
        placeholder = {
            Text(
                text = placeHolder,
                color = if (isSystemInDarkTheme()) SecondContentDark else SecondContentLight
            )
        },
        singleLine = true
    )
}
package org.evolutionsoftware.bookly.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.components.properties.TextFieldProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider

@Composable
fun TextField(
    properties: TextFieldProperties,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    suffix: (@Composable () -> Unit)? = null,
) {
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(value.length),
            ),
        )
    }
    LaunchedEffect(value) {
        if (value != textFieldValue.text) {
            textFieldValue =
                textFieldValue.copy(
                    text = value,
                    selection = TextRange(value.length),
                )
        }
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val state =
        when {
            !enabled || properties.state == TextFieldProperties.State.Disabled -> TextFieldProperties.State.Disabled
            properties.state == TextFieldProperties.State.Error -> TextFieldProperties.State.Error
            isFocused -> TextFieldProperties.State.Focused
            else -> TextFieldProperties.State.Default
        }
    val shape = RoundedCornerShape(TokenProvider.borderRadius.md)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xs),
    ) {
        properties.label?.let {
            Text(
                text = it,
                modifier = Modifier.padding(start = TokenProvider.spacings.xs),
                style = TokenProvider.textStyles.label,
                color = state.getLabelColor(),
            )
        }

        BasicTextField(
            value = textFieldValue,
            onValueChange = { updatedValue ->
                textFieldValue = updatedValue
                onValueChange(updatedValue.text)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = singleLine,
            interactionSource = interactionSource,
            textStyle = TokenProvider.textStyles.input.copy(color = state.getTextColor()),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            cursorBrush = SolidColor(TokenProvider.colors.textBrand),
            decorationBox = { innerTextField ->
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(state.getBackgroundColor(), shape)
                            .border(state.getBorderWidth(), state.getBorderColor(), shape)
                            .padding(start = 22.dp, top = 19.dp, end = 16.dp, bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (textFieldValue.text.isEmpty() && !properties.placeholder.isNullOrBlank()) {
                            Text(
                                text = properties.placeholder,
                                style = TokenProvider.textStyles.input,
                                color = state.getPlaceholderColor(),
                            )
                        }
                        innerTextField()
                    }

                    suffix?.let {
                        Box(
                            modifier = Modifier.padding(start = TokenProvider.spacings.sm),
                            contentAlignment = Alignment.Center,
                        ) {
                            it()
                        }
                    }
                }
            },
        )
    }
}

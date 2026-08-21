package org.evolutionsoftware.bookly.design.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.components.properties.TextFieldProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import androidx.compose.material3.TextField as MaterialTextField

@Composable
fun TextField(
    properties: TextFieldProperties,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    suffix: (@Composable () -> Unit)? = null,
) {
    val isEnabled = enabled && properties.state != TextFieldProperties.State.Disabled
    val isError = properties.state == TextFieldProperties.State.Error

    MaterialTextField(
        value = value,
        onValueChange = onValueChange,
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp),
        enabled = isEnabled,
        readOnly = readOnly,
        isError = isError,
        singleLine = singleLine,
        textStyle = TokenProvider.textStyles.input,
        label =
            properties.label
                ?.takeIf { it.isNotBlank() }
                ?.let { label -> { Text(text = label) } },
        placeholder =
            properties.placeholder
                ?.takeIf { it.isNotBlank() }
                ?.let { placeholder ->
                    {
                        Text(
                            text = placeholder,
                            style = TokenProvider.textStyles.input,
                        )
                    }
                },
        trailingIcon = suffix,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        shape = RoundedCornerShape(TokenProvider.borderRadius.md),
        colors =
            TextFieldDefaults.colors(
                focusedTextColor = TokenProvider.colors.text,
                unfocusedTextColor = TokenProvider.colors.text,
                disabledTextColor = TokenProvider.colors.textMuted.copy(alpha = 0.7f),
                errorTextColor = TokenProvider.colors.text,
                focusedContainerColor = TokenProvider.colors.bgElevated,
                unfocusedContainerColor = TokenProvider.colors.bgElevated,
                disabledContainerColor = TokenProvider.colors.bgElevated.copy(alpha = 0.72f),
                errorContainerColor = TokenProvider.colors.bgElevated,
                cursorColor = TokenProvider.colors.textBrand,
                errorCursorColor = TokenProvider.colors.textDanger,
                focusedIndicatorColor = TokenProvider.colors.borderAccent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = TokenProvider.colors.textDanger,
                focusedLabelColor = TokenProvider.colors.textBrand,
                unfocusedLabelColor = TokenProvider.colors.textMuted,
                disabledLabelColor = TokenProvider.colors.textMuted.copy(alpha = 0.65f),
                errorLabelColor = TokenProvider.colors.textDanger,
                focusedPlaceholderColor = TokenProvider.colors.textMuted.copy(alpha = 0.6f),
                unfocusedPlaceholderColor = TokenProvider.colors.textMuted.copy(alpha = 0.6f),
                disabledPlaceholderColor = TokenProvider.colors.textMuted.copy(alpha = 0.5f),
                errorPlaceholderColor = TokenProvider.colors.textMuted.copy(alpha = 0.6f),
                focusedTrailingIconColor = TokenProvider.colors.textMuted,
                unfocusedTrailingIconColor = TokenProvider.colors.textMuted,
                disabledTrailingIconColor = TokenProvider.colors.textMuted.copy(alpha = 0.7f),
                errorTrailingIconColor = TokenProvider.colors.textMuted,
            ),
    )
}

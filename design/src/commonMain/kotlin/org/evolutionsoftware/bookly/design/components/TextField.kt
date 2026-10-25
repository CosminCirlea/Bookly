package org.evolutionsoftware.bookly.design.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.components.properties.TextFieldProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import androidx.compose.material3.SecureTextField as MaterialSecureTextField
import androidx.compose.material3.TextField as MaterialTextField

/**
 * The [TextFieldState] owns the text and the caret, so edits stay local to the
 * field. That matters in Bookly because hoisted values travel through the MVI
 * container and come back a dispatch later; feeding those stale values back
 * into a field is what makes the caret jump while typing.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextField(
    properties: TextFieldProperties,
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    inputTransformation: InputTransformation? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    contentType: ContentType? = null,
    suffix: (@Composable () -> Unit)? = null,
) {
    MaterialTextField(
        state = state,
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .then(
                    if (contentType != null) {
                        Modifier.semantics { this.contentType = contentType }
                    } else {
                        Modifier
                    },
                ),
        enabled = enabled && properties.state != TextFieldProperties.State.Disabled,
        readOnly = readOnly,
        isError = properties.state == TextFieldProperties.State.Error,
        textStyle = TokenProvider.textStyles.input,
        label = properties.labelContent(),
        placeholder = properties.placeholderContent(),
        trailingIcon = suffix,
        inputTransformation = inputTransformation,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        lineLimits = lineLimits,
        shape = RoundedCornerShape(TokenProvider.borderRadius.md),
        colors = booklyTextFieldColors(),
    )
}

/**
 * Password variant. [isRevealed] drives the obfuscation mode, so the existing
 * eye toggle keeps working.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SecureTextField(
    properties: TextFieldProperties,
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isRevealed: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    contentType: ContentType? = null,
    suffix: (@Composable () -> Unit)? = null,
) {
    MaterialSecureTextField(
        state = state,
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .then(
                    if (contentType != null) {
                        Modifier.semantics { this.contentType = contentType }
                    } else {
                        Modifier
                    },
                ),
        enabled = enabled && properties.state != TextFieldProperties.State.Disabled,
        isError = properties.state == TextFieldProperties.State.Error,
        textStyle = TokenProvider.textStyles.input,
        label = properties.labelContent(),
        placeholder = properties.placeholderContent(),
        trailingIcon = suffix,
        textObfuscationMode =
            if (isRevealed) {
                TextObfuscationMode.Visible
            } else {
                TextObfuscationMode.Hidden
            },
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        shape = RoundedCornerShape(TokenProvider.borderRadius.md),
        colors = booklyTextFieldColors(),
    )
}

private fun TextFieldProperties.labelContent(): (@Composable TextFieldLabelScope.() -> Unit)? =
    label
        ?.takeIf { it.isNotBlank() }
        ?.let { text -> { Text(text = text) } }

private fun TextFieldProperties.placeholderContent(): (@Composable () -> Unit)? =
    placeholder
        ?.takeIf { it.isNotBlank() }
        ?.let { text ->
            {
                Text(
                    text = text,
                    style = TokenProvider.textStyles.input,
                )
            }
        }

@Composable
private fun booklyTextFieldColors(): TextFieldColors =
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
    )

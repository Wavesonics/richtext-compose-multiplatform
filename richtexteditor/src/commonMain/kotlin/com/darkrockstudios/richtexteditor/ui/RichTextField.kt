package com.darkrockstudios.richtexteditor.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.darkrockstudios.richtexteditor.mappers.StyleMapper
import com.darkrockstudios.richtexteditor.transformations.UnorderedListTransformation
import com.darkrockstudios.richtexteditor.transformations.combinedTransformations

private const val EMPTY_STRING = ""

@Composable
internal fun RichTextField(
    value: TextFieldValue,
    styledValue: AnnotatedString,
    styleMapper: StyleMapper,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    textFieldStyle: RichTextFieldStyle = defaultRichTextFieldStyle()
) {
    Box(modifier = modifier) {
        if (value.text.isEmpty()) {
            Text(
                modifier = Modifier.fillMaxSize(),
                text = textFieldStyle.placeholder,
                style = textFieldStyle.textStyle.copy(
                    color = textFieldStyle.placeholderColor
                )
            )
        }
        BasicTextField(
            modifier = Modifier.fillMaxSize(),
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = textFieldStyle.keyboardOptions,
            visualTransformation = combinedTransformations(
                styledValue = styledValue,
                VisualTransformation.None,
                UnorderedListTransformation(styleMapper)
            ),
            textStyle = textFieldStyle.textStyle.copy(
                color = textFieldStyle.textColor
            ),
            cursorBrush = SolidColor(textFieldStyle.cursorColor)
        )
    }
}

@Composable
fun defaultRichTextFieldStyle() = RichTextFieldStyle(
    keyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Sentences,
    ),
    placeholder = EMPTY_STRING,
    textStyle = MaterialTheme.typography.body1,
    textColor = MaterialTheme.colors.onPrimary,
    placeholderColor = MaterialTheme.colors.secondaryVariant,
    cursorColor = MaterialTheme.colors.secondary,
)

data class RichTextFieldStyle(
    val keyboardOptions: KeyboardOptions,
    val placeholder: String,
    val textStyle: TextStyle,
    val textColor: Color,
    val placeholderColor: Color,
    val cursorColor: Color
)

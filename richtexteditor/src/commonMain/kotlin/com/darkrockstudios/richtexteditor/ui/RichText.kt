package com.darkrockstudios.richtexteditor.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.darkrockstudios.richtexteditor.model.RichTextValue
import com.darkrockstudios.richtexteditor.transformations.UnorderedListTransformation
import com.darkrockstudios.richtexteditor.transformations.combinedTransformations

@Composable
fun RichText(
    value: RichTextValue,
    modifier: Modifier = Modifier,
    textStyle: RichTextStyle = defaultRichTextStyle(),
) {
    Text(
        modifier = modifier,
        text = combinedTransformations(
            styledValue = value.styledValue,
            VisualTransformation.None,
            UnorderedListTransformation(value.styleMapper)
        ).filter(value.styledValue).text,
        style = textStyle.textStyle.copy(
            color = textStyle.textColor,
        )
    )
}

@Composable
fun defaultRichTextStyle() = RichTextStyle(
    textStyle = MaterialTheme.typography.bodySmall,
    textColor = MaterialTheme.colorScheme.onSurface,
)

data class RichTextStyle(
    val textStyle: TextStyle,
    val textColor: Color,
)

package com.darkrockstudios.richtexteditor.mappers

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.darkrockstudios.richtexteditor.model.Style

open class StyleMapper {

    open fun fromTag(tag: String): Style {
        val objectInstance = Style.styles.entries.find { (klass, _) ->
            tag.startsWith("${klass.simpleName}/")
        }?.value

        if (objectInstance != null) {
            return objectInstance
        }

        return when {
            tag.startsWith("${Style.TextColor::class.simpleName}/") -> {
                val value = tag.substringAfter("${Style.TextColor::class.simpleName}/")
                Style.TextColor(Color(value.toULongOrNull() ?: 0UL))
            }
            tag.startsWith("${Style.TextSize::class.simpleName}/") -> {
                val value = tag.substringAfter("${Style.TextSize::class.simpleName}/")
                Style.TextSize(value.toFloatOrNull() ?: 1f)
            }
            else -> throw IllegalArgumentException("No style found for $tag")
        }
    }

    open fun toTag(style: Style, simple: Boolean = false): String = if (simple) {
        "${style::class.simpleName}/"
    } else {
        when (style) {
            is Style.TextSize -> "${style::class.simpleName}/${style.fraction}"
            is Style.TextColor -> "${style::class.simpleName}/${style.color?.value}"
            else -> "${style::class.simpleName}/"
        }
    }

    @ExperimentalUnitApi
    open fun toSpanStyle(style: Style): SpanStyle? = when (style) {
        Style.Bold -> SpanStyle(fontWeight = FontWeight.Bold)
        Style.Underline -> SpanStyle(textDecoration = TextDecoration.Underline)
        Style.Italic -> SpanStyle(fontStyle = FontStyle.Italic)
        Style.Strikethrough -> SpanStyle(textDecoration = TextDecoration.LineThrough)
        is Style.TextColor -> SpanStyle(color = requireNotNull(style.color))
        is Style.TextSize -> SpanStyle(
            fontSize = TextUnit(
                requireNotNull(style.fraction),
                TextUnitType.Em
            )
        )
        else -> null
    }

    open fun toParagraphStyle(style: Style): ParagraphStyle? = when (style) {
        Style.AlignLeft -> ParagraphStyle(textAlign = TextAlign.Left)
        Style.AlignCenter -> ParagraphStyle(textAlign = TextAlign.Center)
        Style.AlignRight -> ParagraphStyle(textAlign = TextAlign.Right)
        Style.UnorderedList -> TODO()
        Style.OrderedList -> TODO()
        else -> null
    }
}

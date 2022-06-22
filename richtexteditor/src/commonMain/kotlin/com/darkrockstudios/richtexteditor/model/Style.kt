package com.darkrockstudios.richtexteditor.model

import androidx.compose.ui.graphics.Color

interface Style {

    object ClearFormat : Style

    interface TextStyle : Style
    interface ParagraphStyle : Style

    object OrderedList : ParagraphStyle
    object UnorderedList : ParagraphStyle
    object AlignLeft : ParagraphStyle
    object AlignCenter : ParagraphStyle
    object AlignRight : ParagraphStyle

    object Bold : TextStyle
    object Underline : TextStyle
    object Italic : TextStyle
    object Strikethrough : TextStyle

    class TextColor(val color: Color? = null) : TextStyle

    class TextSize(
        fraction: Float = DEFAULT_VALUE
    ) : TextStyle {

        var fraction: Float? = fraction.coerceIn(
            minimumValue = MIN_VALUE.toFloat(),
            maximumValue = MAX_VALUE.toFloat()
        )

        companion object {
            const val DEFAULT_VALUE = 1f
            const val MIN_VALUE = 0.5
            const val MAX_VALUE = 2.0
        }
    }

    companion object {
        // Kotlin/Native doesn't support enough reflection,
        // so we need to supply this list out selves
        val styles = mapOf(
            ClearFormat::class to ClearFormat,
            OrderedList::class to OrderedList,
            UnorderedList::class to UnorderedList,
            AlignLeft::class to AlignLeft,
            AlignCenter::class to AlignCenter,
            AlignRight::class to AlignRight,
            Bold::class to Bold,
            Underline::class to Underline,
            Italic::class to Italic,
            Strikethrough::class to Strikethrough
        )
    }
}


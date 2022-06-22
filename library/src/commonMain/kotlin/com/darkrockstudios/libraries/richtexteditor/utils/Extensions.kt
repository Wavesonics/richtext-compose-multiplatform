package com.darkrockstudios.libraries.richtexteditor.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import com.darkrockstudios.libraries.richtexteditor.common.systemLineSeparator

internal fun AnnotatedString.copy(
    text: String = this.text,
    spanStyles: List<AnnotatedString.Range<SpanStyle>> = this.spanStyles,
    paragraphStyles: List<AnnotatedString.Range<ParagraphStyle>> = this.paragraphStyles
) = AnnotatedString(
    text = text,
    spanStyles = spanStyles,
    paragraphStyles = paragraphStyles
)

internal fun Int.coerceStartOfParagraph(text: String): Int {
    val previousNewLineCharacterIndex = text.substring(0, this)
        .lastIndexOf(systemLineSeparator())

    if (previousNewLineCharacterIndex == -1) {
        return 0
    }

    return previousNewLineCharacterIndex + systemLineSeparator().length
}

internal fun Int.coerceEndOfParagraph(text: String): Int {
    val nextNewLineCharacterIndex = text.substring(this)
        .indexOf(systemLineSeparator())

    if (nextNewLineCharacterIndex == -1) {
        return text.length
    }

    return this + nextNewLineCharacterIndex + 1
}

internal fun TextRange.coerceParagraph(text: String) = TextRange(
    start = start.coerceStartOfParagraph(text),
    end = end.coerceEndOfParagraph(text)
)

internal fun TextRange.coerceNotReversed() = if (start < end) {
    this
} else {
    TextRange(end, start)
}

internal fun String.startsWith(prefixes: Set<String>) =
    prefixes.any { this.startsWith(it) }

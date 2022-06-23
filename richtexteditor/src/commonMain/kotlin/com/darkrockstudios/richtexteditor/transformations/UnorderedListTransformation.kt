package com.darkrockstudios.richtexteditor.transformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.darkrockstudios.richtexteditor.common.systemLineSeparator
import com.darkrockstudios.richtexteditor.mappers.StyleMapper
import com.darkrockstudios.richtexteditor.model.Style
import com.darkrockstudios.richtexteditor.utils.AnnotatedStringBuilder

class UnorderedListTransformation(private val styleMapper: StyleMapper) : VisualTransformation {

    companion object {
        const val DOT_CHARACTER = "\t• "
    }

    override fun filter(text: AnnotatedString): TransformedText {
        var outputText = text.text
        val ranges = mutableMapOf<AnnotatedString.Range<*>, Int>()
        val outputParagraphs = text.paragraphStyles.map {
            AnnotatedStringBuilder.MutableRange.fromRange(it)
        }

        outputParagraphs.filter {
            it.tag == styleMapper.toTag(Style.UnorderedList)
        }.forEach { style ->
            val subtext = outputText.substring(style.start, style.end)
            val lines = subtext.split(systemLineSeparator()).map {
                "$DOT_CHARACTER$it"
            }
            outputText = outputText.substring(0, style.start - 1) +
                    lines.joinToString(systemLineSeparator()) +
                    outputText.substring(style.end)

            style.end += lines.size * DOT_CHARACTER.length

            var offset = 0
            lines.forEachIndexed { index, line ->
                ranges[AnnotatedString.Range(
                    null,
                    style.start + offset,
                    style.start + offset + line.length
                )] = (index + 1) * DOT_CHARACTER.length
                offset += line.length
            }
        }

        return TransformedText(
            AnnotatedString(
                text = outputText,
                spanStyles = text.spanStyles,
                paragraphStyles = outputParagraphs.map { it.toRange() }
            ),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    var output = offset
                    ranges.forEach {
                        if (offset >= it.key.start && offset <= it.key.end) {
                            output += it.value
                        }
                    }
                    return output
                }

                override fun transformedToOriginal(offset: Int): Int {
                    var output = offset
                    ranges.forEach {
                        if (offset >= it.key.start && offset <= it.key.end) {
                            output -= it.value
                        }
                    }
                    return output
                }
            }
        )
    }
}

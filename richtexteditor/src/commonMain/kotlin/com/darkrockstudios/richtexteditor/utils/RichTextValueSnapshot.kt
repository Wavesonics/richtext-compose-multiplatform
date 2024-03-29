package com.darkrockstudios.richtexteditor.utils

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.darkrockstudios.richtexteditor.mappers.StyleMapper

/**
 * A helper class that lets you serialize the [RichTextValue]
 */
@Parcelize
data class RichTextValueSnapshot(
    val text: String,
    val spanStyles: List<RichTextValueSpanSnapshot> = emptyList(),
    val paragraphStyles: List<RichTextValueSpanSnapshot> = emptyList(),
    val selectionPosition: Int = 0,
) : Parcelable {

    internal fun toAnnotatedStringBuilder(styleMapper: StyleMapper): AnnotatedStringBuilder {
        val spans = this.spanStyles.map {
            val item = styleMapper.toSpanStyle(styleMapper.fromTag(it.tag)) ?: return@map null
            AnnotatedStringBuilder.MutableRange(item, it.start, it.end, it.tag)
        }.filterNotNull()

        val paragraphs = this.paragraphStyles.map {
            val item = styleMapper.toParagraphStyle(styleMapper.fromTag(it.tag)) ?: return@map null
            AnnotatedStringBuilder.MutableRange(item, it.start, it.end, it.tag)
        }.filterNotNull()

        return AnnotatedStringBuilder().apply {
            text = this@RichTextValueSnapshot.text
            addSpans(*spans.toTypedArray())
            addParagraphs(*paragraphs.toTypedArray())
        }
    }

    @Parcelize
    data class RichTextValueSpanSnapshot(
        val start: Int,
        val end: Int,
        val tag: String,
    ) : Parcelable

    companion object {
        internal fun fromAnnotatedStringBuilder(
            annotatedStringBuilder: AnnotatedStringBuilder,
            selectionPosition: Int,
        ) = RichTextValueSnapshot(
            text = annotatedStringBuilder.text,
            spanStyles = annotatedStringBuilder.spanStyles.map {
                it.toRichTextValueSpanSnapshot()
            },
            paragraphStyles = annotatedStringBuilder.paragraphStyles.map {
                it.toRichTextValueSpanSnapshot()
            },
            selectionPosition = selectionPosition
        )
    }
}

private fun <T> AnnotatedStringBuilder.MutableRange<T>.toRichTextValueSpanSnapshot() =
    RichTextValueSnapshot.RichTextValueSpanSnapshot(start = start, end = end, tag = tag)

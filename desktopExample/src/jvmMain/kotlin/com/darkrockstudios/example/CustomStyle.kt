package com.darkrockstudios.example

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import com.darkrockstudios.libraries.richtexteditor.mappers.StyleMapper
import com.darkrockstudios.libraries.richtexteditor.model.Style

object BoldRedStyle : Style

class CustomStyleMapper : StyleMapper() {

    override fun fromTag(tag: String) =
        runCatching { super.fromTag(tag) }.getOrNull() ?: when (tag) {
            "${BoldRedStyle.javaClass.simpleName}/" -> BoldRedStyle
            else -> throw IllegalArgumentException()
        }

    override fun toSpanStyle(style: Style) = super.toSpanStyle(style) ?: when (style) {
        is BoldRedStyle -> SpanStyle(
            color = Color.Red,
            fontWeight = FontWeight.Bold,
        )
        else -> null
    }
}
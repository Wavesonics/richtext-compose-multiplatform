package com.darkrockstudios.example

import androidx.compose.ui.graphics.Color

object ColorUtils {
    fun HSLToColor(hsl: FloatArray): ULong {
        val h = hsl[0]
        val s = hsl[1]
        val l = hsl[2]
        val c = (1f - Math.abs(2 * l - 1f)) * s
        val m = l - 0.5f * c
        val x = c * (1f - Math.abs(h / 60f % 2f - 1f))
        val hueSegment = h.toInt() / 60
        var r = 0
        var g = 0
        var b = 0
        when (hueSegment) {
            0 -> {
                r = Math.round(255 * (c + m))
                g = Math.round(255 * (x + m))
                b = Math.round(255 * m)
            }
            1 -> {
                r = Math.round(255 * (x + m))
                g = Math.round(255 * (c + m))
                b = Math.round(255 * m)
            }
            2 -> {
                r = Math.round(255 * m)
                g = Math.round(255 * (c + m))
                b = Math.round(255 * (x + m))
            }
            3 -> {
                r = Math.round(255 * m)
                g = Math.round(255 * (x + m))
                b = Math.round(255 * (c + m))
            }
            4 -> {
                r = Math.round(255 * (x + m))
                g = Math.round(255 * m)
                b = Math.round(255 * (c + m))
            }
            5, 6 -> {
                r = Math.round(255 * (c + m))
                g = Math.round(255 * m)
                b = Math.round(255 * (x + m))
            }
        }
        r = constrain(r, 0, 255)
        g = constrain(g, 0, 255)
        b = constrain(b, 0, 255)
        return Color(r, g, b).value
    }

    private fun constrain(amount: Int, low: Int, high: Int): Int {
        return if (amount < low) low else amount.coerceAtMost(high)
    }
}

internal fun Int.hueToColor(saturation: Float = 1f, value: Float = 0.5f): Color = Color(
    ColorUtils.HSLToColor(floatArrayOf(this.toFloat(), saturation, value))
)
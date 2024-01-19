package com.darkrockstudios.example.richtexteditor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.darkrockstudios.richtexteditor.mappers.StyleMapper
import com.darkrockstudios.richtexteditor.model.RichTextValue
import com.darkrockstudios.richtexteditor.model.Style
import com.darkrockstudios.richtexteditor.ui.RichText
import com.darkrockstudios.richtexteditor.ui.RichTextEditor
import com.darkrockstudios.richtexteditor.ui.defaultRichTextFieldStyle
import com.darkrockstudios.richtexteditor.ui.defaultRichTextStyle
import kotlin.random.Random

private val text =
    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
            "Why do we use it?\n" +
            "\n" +
            "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n" +
            "\n" +
            "Where does it come from?\n" +
            "\n" +
            "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.\n" +
            "\n" +
            "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham.\n" +
            "Where can I get some?\n" +
            "\n" +
            "There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc."


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                var value by remember {
                    mutableStateOf(
                        RichTextValue.fromString(
                            text = text,
                            // Optional parameter; leave it blank if you want to use provided styles
                            // But if you want to customize the user experience you're free to do that
                            // by providing a custom StyleMapper
                            styleMapper = CustomStyleMapper()
                        )
                    )
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    RichTextEditor(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f)
                            .padding(16.dp),
                        value = value,
                        onValueChange = { value = it },
                        textFieldStyle = defaultRichTextFieldStyle().copy(
                            textColor = Color.Black,
                            placeholderColor = Color.LightGray,
                            placeholder = "My rich text editor in action"
                        )
                    )

                    // If you want to display formatted text you can use [RichText] instead of [RichTextEditor]
                    RichText(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray)
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        value = value,
                        textStyle = defaultRichTextStyle().copy(
                            textColor = Color.Black,
                        )
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(Color.DarkGray)
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Button for a custom style
                        IconButton(onClick = {
                            value = value.insertStyle(BoldRedStyle)
                        }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.icon_bold),
                                tint = if (value.currentStyles.contains(BoldRedStyle)) {
                                    Color.Red
                                } else {
                                    Color.Red.copy(alpha = 0.3f)
                                },
                                contentDescription = null
                            )
                        }

                        EditorAction(
                            iconRes = R.drawable.icon_bold,
                            active = value.currentStyles.contains(Style.Bold)
                        ) {
                            value = value.insertStyle(Style.Bold)
                        }
                        EditorAction(
                            iconRes = R.drawable.icon_underline,
                            active = value.currentStyles.contains(Style.Underline)
                        ) {
                            value = value.insertStyle(Style.Underline)
                        }
                        EditorAction(
                            iconRes = R.drawable.icon_italic,
                            active = value.currentStyles.contains(Style.Italic)
                        ) {
                            value = value.insertStyle(Style.Italic)
                        }
                        EditorAction(
                            iconRes = R.drawable.icon_strikethrough,
                            active = value.currentStyles.contains(Style.Strikethrough)
                        ) {
                            value = value.insertStyle(Style.Strikethrough)
                        }
                        EditorAction(
                            iconRes = R.drawable.icon_align_left,
                            active = value.currentStyles.contains(Style.AlignLeft)
                        ) {
                            value = value.insertStyle(Style.AlignLeft)
                        }
                        EditorAction(
                            iconRes = R.drawable.icon_align_center,
                            active = value.currentStyles.contains(Style.AlignCenter)
                        ) {
                            value = value.insertStyle(Style.AlignCenter)
                        }
                        EditorAction(
                            iconRes = R.drawable.icon_align_right,
                            active = value.currentStyles.contains(Style.AlignRight)
                        ) {
                            value = value.insertStyle(Style.AlignRight)
                        }
                        EditorAction(
                            iconRes = R.drawable.icon_text_size,
                            active = value.currentStyles
                                .filterIsInstance<Style.TextSize>()
                                .isNotEmpty()
                        ) {
                            // Remove all styles in selected region that changes the text size
                            value = value.clearStyles(Style.TextSize())

                            // Here you would show a dialog of some sorts and allow user to pick
                            // a specific text size. I'm gonna use a random one between 50% and 200%

                            value = value.insertStyle(
                                Style.TextSize(
                                    (Random.nextFloat() *
                                            (Style.TextSize.MAX_VALUE - Style.TextSize.MIN_VALUE) +
                                            Style.TextSize.MIN_VALUE).toFloat()
                                )
                            )
                        }
                        EditorAction(
                            iconRes = R.drawable.icon_circle,
                            active = value.currentStyles
                                .filterIsInstance<Style.TextColor>()
                                .isNotEmpty()
                        ) {
                            // Remove all styles in selected region that changes the text color
                            value = value.clearStyles(Style.TextColor())

                            // Here you would show a dialog of some sorts and allow user to pick
                            // a specific color. I'm gonna use a random one

                            value = value.insertStyle(
                                Style.TextColor(Random.nextInt(360).hueToColor())
                            )
                        }
                        EditorAction(R.drawable.icon_format_clear, active = true) {
                            value = value.insertStyle(Style.ClearFormat)
                        }
                        EditorAction(
                            iconRes = R.drawable.icon_undo,
                            active = value.isUndoAvailable
                        ) {
                            value = value.undo()
                        }
                        EditorAction(
                            iconRes = R.drawable.icon_redo,
                            active = value.isRedoAvailable
                        ) {
                            value = value.redo()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun EditorAction(
        @DrawableRes iconRes: Int,
        active: Boolean,
        onClick: () -> Unit,
    ) {
        IconButton(onClick = onClick) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = iconRes),
                tint = if (active) Color.White else Color.Black,
                contentDescription = null
            )
        }
    }

    private fun Int.hueToColor(saturation: Float = 1f, value: Float = 0.5f): Color = Color(
        ColorUtils.HSLToColor(floatArrayOf(this.toFloat(), saturation, value))
    )
}

object BoldRedStyle : Style

class CustomStyleMapper : StyleMapper() {

    override fun fromTag(tag: String) =
        runCatching { super.fromTag(tag) }.getOrNull() ?: when (tag) {
            "${BoldRedStyle.javaClass.simpleName}/" -> BoldRedStyle
            else -> throw IllegalArgumentException()
        }

    @ExperimentalUnitApi
    override fun toSpanStyle(style: Style) = super.toSpanStyle(style) ?: when (style) {
        is BoldRedStyle -> SpanStyle(
            color = Color.Red,
            fontWeight = FontWeight.Bold,
        )
        else -> null
    }
}

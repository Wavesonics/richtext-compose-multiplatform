package com.darkrockstudios.example

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.darkrockstudios.richtexteditor.model.RichTextValue
import com.darkrockstudios.richtexteditor.model.Style
import com.darkrockstudios.richtexteditor.ui.RichText
import com.darkrockstudios.richtexteditor.ui.RichTextEditor
import com.darkrockstudios.richtexteditor.ui.defaultRichTextFieldStyle
import com.darkrockstudios.richtexteditor.ui.defaultRichTextStyle
import kotlin.random.Random

private const val text = """
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent nisi nisi, pellentesque ac ultricies sed, varius at nunc. Integer vitae semper justo. Ut non nunc vel dolor interdum consectetur. Nam ullamcorper molestie odio, vel aliquet est viverra at. Quisque lectus sapien, finibus nec tincidunt in, vulputate vitae neque. Suspendisse mollis elit velit, a pulvinar eros dictum a. Nunc in ornare metus, sed dapibus nibh. Pellentesque suscipit enim non ultricies auctor. In elit metus, vulputate at turpis ut, scelerisque dictum ipsum. Curabitur in nisl auctor mi lobortis tincidunt. In ultrices luctus bibendum. Proin volutpat, purus tempor iaculis pulvinar, leo erat malesuada mi, nec sollicitudin metus erat commodo velit. Sed in diam sed ante convallis pharetra sit amet eget diam.

Integer massa mauris, commodo sit amet quam id, vulputate commodo ante. Morbi felis purus, aliquam sit amet mattis et, pulvinar eu augue. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vivamus pretium quam sit amet nisl tempus pretium. Aliquam nibh ipsum, porta quis nulla iaculis, malesuada ullamcorper purus. Phasellus eu fringilla lorem. Praesent condimentum felis in dui dictum scelerisque. Maecenas quis dictum leo. Fusce at fringilla mauris. Vivamus vel metus eget dui condimentum efficitur a sed ante. Vestibulum finibus nisl tincidunt tempor semper. Etiam purus felis, tempus in ultricies sit amet, fermentum vitae lorem. Duis aliquet, ipsum et ornare pretium, dolor diam pellentesque sapien, a maximus velit ligula id lorem.

Vestibulum vulputate ultricies vulputate. In ut hendrerit mauris. Maecenas sit amet dui justo. Nullam egestas eros ut justo rutrum egestas. Fusce ultrices lobortis ex, sed placerat tellus tincidunt at. Duis congue ornare suscipit. Mauris blandit dapibus fermentum. In id libero posuere, pharetra enim non, malesuada nisi. Donec ac eros sed elit congue auctor. Maecenas eros purus, rhoncus at dui accumsan, dapibus consequat magna. Nunc nec rhoncus mi. In placerat velit at velit tincidunt, sed feugiat enim malesuada. Phasellus libero arcu, fermentum congue libero sed, tincidunt porttitor mauris. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Morbi in magna ullamcorper, malesuada nibh nec, porta lectus.

Nullam nec lorem elit. Sed quis elementum nibh. Nam congue mauris ac aliquet imperdiet. Donec vulputate orci et pretium viverra. Vivamus euismod auctor tristique. Sed velit est, ornare quis arcu quis, hendrerit auctor massa. Lorem ipsum dolor sit amet, consectetur adipiscing elit.

Phasellus varius at augue eu interdum. Integer dapibus egestas risus, vitae tempor lorem elementum in. Cras euismod tellus quis eleifend dictum. Aliquam quis pharetra tellus. Cras molestie auctor sollicitudin. Nam et fringilla lacus, mollis auctor massa. Quisque pretium aliquet augue ac rutrum. Donec at consectetur odio, at rutrum nisl. 
"""

fun main() = application {
    Window(
        title = "RichText Compose",
        onCloseRequest = ::exitApplication
    ) {
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
                            painter = painterResource("images/icon_bold.xml"),
                            tint = if (value.currentStyles.contains(BoldRedStyle)) {
                                Color.Red
                            } else {
                                Color.Red.copy(alpha = 0.3f)
                            },
                            contentDescription = null
                        )
                    }

                    EditorAction(
                        iconRes = "icon_bold",
                        active = value.currentStyles.contains(Style.Bold)
                    ) {
                        value = value.insertStyle(Style.Bold)
                    }
                    EditorAction(
                        iconRes = "icon_underline",
                        active = value.currentStyles.contains(Style.Underline)
                    ) {
                        value = value.insertStyle(Style.Underline)
                    }
                    EditorAction(
                        iconRes = "icon_italic",
                        active = value.currentStyles.contains(Style.Italic)
                    ) {
                        value = value.insertStyle(Style.Italic)
                    }
                    EditorAction(
                        iconRes = "icon_strikethrough",
                        active = value.currentStyles.contains(Style.Strikethrough)
                    ) {
                        value = value.insertStyle(Style.Strikethrough)
                    }
                    EditorAction(
                        iconRes = "icon_align_left",
                        active = value.currentStyles.contains(Style.AlignLeft)
                    ) {
                        value = value.insertStyle(Style.AlignLeft)
                    }
                    EditorAction(
                        iconRes = "icon_align_center",
                        active = value.currentStyles.contains(Style.AlignCenter)
                    ) {
                        value = value.insertStyle(Style.AlignCenter)
                    }
                    EditorAction(
                        iconRes = "icon_align_right",
                        active = value.currentStyles.contains(Style.AlignRight)
                    ) {
                        value = value.insertStyle(Style.AlignRight)
                    }
                    EditorAction(
                        iconRes = "icon_text_size",
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
                        iconRes = "icon_circle",
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
                    EditorAction("icon_format_clear", active = true) {
                        value = value.insertStyle(Style.ClearFormat)
                    }
                    EditorAction(
                        iconRes = "icon_undo",
                        active = value.isUndoAvailable
                    ) {
                        value = value.undo()
                    }
                    EditorAction(
                        iconRes = "icon_redo",
                        active = value.isRedoAvailable
                    ) {
                        value = value.redo()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun EditorAction(
    iconRes: String,
    active: Boolean,
    onClick: () -> Unit,
) {
    /*
    // https://github.com/JetBrains/compose-jb/issues/2569
    IconButton(
        onClick = onClick,
        modifier = Modifier.focusable(false)
    ) {
    */
    Box(
        modifier = Modifier.onClick { onClick() }
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource("images/$iconRes.xml"),
            tint = if (active) Color.White else Color.Black,
            contentDescription = null
        )
    }
}
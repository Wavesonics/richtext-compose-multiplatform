package com.darkrockstudios.richtexteditor.common

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual fun systemLineSeparator(): String = System.lineSeparator()
actual val defaultDispatcher: CoroutineContext = Dispatchers.Default
actual val uiDispatcher: CoroutineContext = Dispatchers.Main
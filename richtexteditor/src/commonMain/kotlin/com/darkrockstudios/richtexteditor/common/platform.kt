package com.darkrockstudios.richtexteditor.common

import kotlin.coroutines.CoroutineContext

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect fun systemLineSeparator(): String

expect val defaultDispatcher: CoroutineContext
expect val uiDispatcher: CoroutineContext
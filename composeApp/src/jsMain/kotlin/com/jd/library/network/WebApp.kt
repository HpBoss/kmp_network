package com.jd.library.network

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        val lifecycle = LifecycleRegistry()
        // ...
        lifecycle.resume()
        CanvasBasedWindow("MusicApp-KMP") {

        }
    }
}




package com.mohitkumar.kmpdemo

import androidx.compose.ui.window.ComposeUIViewController
import com.mohitkumar.kmpdemo.data.DatabaseDriverFactory

fun MainViewController() = ComposeUIViewController { App(DatabaseDriverFactory()) }

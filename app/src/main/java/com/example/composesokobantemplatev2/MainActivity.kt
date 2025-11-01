package com.example.composesokobantemplatev2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.composesokobantemplatev2.ui.theme.ComposeSokobanTemplateV2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSokobanTemplateV2Theme {
                AppNav()
            }
        }
    }
}
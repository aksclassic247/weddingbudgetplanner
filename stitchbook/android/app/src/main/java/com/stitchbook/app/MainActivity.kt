package com.stitchbook.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.stitchbook.app.ui.StitchBookApp
import com.stitchbook.app.ui.theme.StitchBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StitchBookTheme {
                StitchBookApp()
            }
        }
    }
}

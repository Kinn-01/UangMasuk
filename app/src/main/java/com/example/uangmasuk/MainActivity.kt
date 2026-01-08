package com.example.uangmasuk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.uangmasuk.navigation.AppNavHost
import com.example.uangmasuk.ui.theme.UangMasukTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UangMasukTheme {
                AppNavHost()
            }
        }
    }
}

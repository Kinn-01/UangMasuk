package com.example.uangmasuk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
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

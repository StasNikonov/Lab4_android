package com.example.lab4android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var mediaType by remember { mutableStateOf<String?>(null) }

                if (mediaType == null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = { mediaType = "audio" }) {
                            Text("Програвати Аудіо")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { mediaType = "video" }) {
                            Text("Програвати Відео")
                        }
                    }
                } else {
                    when (mediaType) {
                        "audio" -> AudioPlayerScreen()
                        "video" -> VideoPlayerScreen{ intent ->
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}

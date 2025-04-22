package com.example.lab4android

import android.content.Intent
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri

@Composable
fun VideoPlayerScreen(onLaunchIntent: (Intent) -> Unit) {
    var url by remember {
        mutableStateOf("https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
    }

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("Введи URL відео") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType("https://drive.google.com".toUri(), "text/html")
                        addCategory(Intent.CATEGORY_BROWSABLE)
                    }
                    onLaunchIntent(intent)
                }
            ) {
                Text("Завантажити з девайсу")
            }

            Button(
                onClick = {
                    url = url.trim()
                }
            ) {
                Text("Оновити відео")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AndroidView(
            factory = { context ->
                VideoView(context).apply {
                    setVideoURI(url.toUri())
                    val controller = MediaController(context)
                    controller.setAnchorView(this)
                    setMediaController(controller)
                    requestFocus()
                    start()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}

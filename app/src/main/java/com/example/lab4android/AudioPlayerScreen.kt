package com.example.lab4android

import android.media.MediaPlayer
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf

@Composable
fun AudioPlayerScreen() {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer() }
    var audioUri by remember { mutableStateOf<Uri?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) } // Додано для перевірки стану паузи
    var currentPosition by remember { mutableStateOf(0) } // Зберігаємо поточну позицію

    // Лаунчер для вибору аудіо з пристрою
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            audioUri = it
            mediaPlayer.reset()
            mediaPlayer.setDataSource(context, it)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
                isPlaying = true
                isPaused = false
            }
        }
    }

    var url by remember { mutableStateOf("") }
    var showUrlDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Аудіо Програвач", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(16.dp))

        // Кнопка для вибору аудіо з пристрою
        Button(onClick = { launcher.launch("audio/*") }) {
            Text("Обрати аудіо з пристрою")
        }

        Spacer(Modifier.height(8.dp))

        // Кнопка для завантаження аудіо з Інтернету
        Button(onClick = { showUrlDialog = true }) {
            Text("Завантажити аудіо з Інтернету")
        }

        Spacer(Modifier.height(16.dp))

        Row {
            // Кнопка Play
            Button(
                onClick = {
                    if (isPaused) {
                        mediaPlayer.seekTo(currentPosition) // Відновлюємо з поточної позиції
                        mediaPlayer.start()
                        isPlaying = true
                        isPaused = false
                    } else if (!isPlaying) {
                        mediaPlayer.start()
                        isPlaying = true
                    }
                },
                enabled = !isPlaying || isPaused
            ) {
                Text("Play")
            }

            Spacer(Modifier.width(8.dp))

            // Кнопка Pause
            Button(
                onClick = {
                    if (mediaPlayer.isPlaying) {
                        currentPosition = mediaPlayer.currentPosition // Зберігаємо поточну позицію
                        mediaPlayer.pause()
                        isPlaying = false
                        isPaused = true
                    }
                },
                enabled = isPlaying
            ) {
                Text("Pause")
            }

            Spacer(Modifier.width(8.dp))

            // Кнопка Stop
            Button(
                onClick = {
                    mediaPlayer.stop()
                    isPlaying = false
                    isPaused = false
                    currentPosition = 0 // Скидаємо позицію на початок
                    mediaPlayer.reset()
                },
                enabled = isPlaying || isPaused
            ) {
                Text("Stop")
            }
        }

        // Діалог для введення URL аудіо
        if (showUrlDialog) {
            AlertDialog(
                onDismissRequest = { showUrlDialog = false },
                title = { Text("URL аудіо") },
                text = {
                    OutlinedTextField(
                        value = url,
                        onValueChange = { url = it },
                        label = { Text("https://...") }
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        try {
                            mediaPlayer.reset()
                            mediaPlayer.setDataSource(url)  // Вказуємо URL аудіофайлу
                            mediaPlayer.prepareAsync()  // Завантаження асинхронно
                            mediaPlayer.setOnPreparedListener {
                                // Коли аудіофайл готовий, розпочати відтворення
                                mediaPlayer.start()
                                isPlaying = true
                                isPaused = false
                            }
                            audioUri = null // позначаємо, що аудіофайл з Інтернету
                        } catch (e: Exception) {
                            e.printStackTrace()  // Лог помилки
                        }
                        showUrlDialog = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    Button(onClick = { showUrlDialog = false }) {
                        Text("Скасувати")
                    }
                }
            )
        }
    }
}


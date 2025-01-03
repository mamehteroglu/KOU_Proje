package com.example.kou_proje

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.kou_proje.ui.theme.KOU_ProjeTheme

class TaskScreenActivity : ComponentActivity() {
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KOU_ProjeTheme {
                TaskScreen(viewModel = viewModel)
            }
        }
    }
}
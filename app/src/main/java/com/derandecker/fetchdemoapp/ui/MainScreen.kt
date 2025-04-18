package com.derandecker.fetchdemoapp.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.derandecker.fetchdemoapp.viewmodel.MainScreenViewModel

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val viewModel: MainScreenViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn {
        items(
            items = uiState.itemList, key = { item -> item.id },
        ) { item ->
            Text("${item.id}, ${item.listId}, ${item.name}")
        }
    }
}

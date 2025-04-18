package com.derandecker.fetchdemoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.derandecker.fetchdemoapp.viewmodel.Item
import com.derandecker.fetchdemoapp.viewmodel.LoadingState
import com.derandecker.fetchdemoapp.viewmodel.MainScreenViewModel
import java.util.UUID

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val viewModel: MainScreenViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState.loadingState) {
        LoadingState.Loading -> LoadingScreen(
            modifier = modifier.padding(
                start = 16.dp,
                end = 16.dp
            )
        )

        is LoadingState.Error -> ErrorScreen(
            modifier = modifier.padding(
                start = 16.dp,
                end = 16.dp
            ), message = state.message
        )

        LoadingState.Success -> SuccessScreen(
            modifier = modifier.padding(start = 16.dp, end = 16.dp),
            items = uiState.itemList
        )
    }
}

@Composable
fun LoadingScreen(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(modifier: Modifier, message: String) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message)
    }
}

@Composable
fun SuccessScreen(modifier: Modifier, items: List<Item>) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        items(
            items = items, key = { item -> item.id ?: UUID.randomUUID() },
        ) { item ->
            Text("${item.id}, ${item.listId}, ${item.name}")
        }
    }
}


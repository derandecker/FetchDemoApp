package com.derandecker.fetchdemoapp.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.derandecker.fetchdemoapp.ui.viewmodel.Item
import com.derandecker.fetchdemoapp.ui.viewmodel.LoadingState
import com.derandecker.fetchdemoapp.ui.viewmodel.MainScreenViewModel
import java.util.UUID

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val viewModel: MainScreenViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val itemList = uiState.itemList

    val groupedList = remember(itemList) {
        itemList.groupBy { it.listId }
    }

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
            groupedList = groupedList
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SuccessScreen(modifier: Modifier, groupedList: Map<Int?, List<Item>>) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        groupedList.forEach { (listId, itemsInListId) ->
            stickyHeader {
                Text(
                    text = "List ID $listId",
                    modifier = Modifier
                        .fillMaxWidth()
                        // Color works well with dark theme as well
                        // but we could use if(isSystemInDarkTheme()) Color.White else Color.Blue
                        // to change the color depending on light / dark theme user setting
                        .background(Color.Blue),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            items(
                items = itemsInListId, key = { item -> item.id ?: UUID.randomUUID() },
            ) { item ->
                Text("${item.name}")
            }
        }
    }
}

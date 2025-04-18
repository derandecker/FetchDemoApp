package com.derandecker.fetchdemoapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainScreenViewModel: ViewModel() {

    data class UiState(
        val loadingState: LoadingState = LoadingState.Loading,
        val itemList: List<Item> = emptyList()
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }


    private fun loadData() {

    }

}

data class Item(
    val id: Int,
    val listId: Int,
    val name: String
)

sealed class LoadingState {
    object Loading : LoadingState()
    object Empty : LoadingState()
    data class Error(val message: String) : LoadingState()
    object Success : LoadingState()
}

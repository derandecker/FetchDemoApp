package com.derandecker.fetchdemoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.derandecker.fetchdemoapp.network.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.net.SocketTimeoutException
import java.net.UnknownHostException

const val BASE_URL = "https://fetch-hiring.s3.amazonaws.com/"

class MainScreenViewModel : ViewModel() {

    data class UiState(
        val loadingState: LoadingState = LoadingState.Loading,
        val itemList: List<Item> = emptyList()
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private lateinit var networkService: NetworkService

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // would normally set this up as a Singleton as part of DI.
                // set up in ViewModel for time savings and due to the simplicity of
                // this project.
                val contentType = "application/json".toMediaType()
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(Json.asConverterFactory(contentType))
                    .build()
                networkService = retrofit.create(NetworkService::class.java)
                loadData()
            }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun loadData() {
        // would normally do this in a repo, but as noted above, due to simplicity
        // of this project, doing network call here in ViewModel.
        val response = try {
            networkService.getItems()
        } catch (e: UnknownHostException) {
            _uiState.update {
                it.copy(
                    loadingState = LoadingState.Error(e.message.toString())
                )
            }
            return
        } catch (e: MissingFieldException) {
            _uiState.update {
                it.copy(
                    loadingState = LoadingState.Error(e.message.toString())
                )
            }
            return
        } catch (e: SocketTimeoutException) {
            _uiState.update {
                it.copy(
                    loadingState = LoadingState.Error(e.message.toString())
                )
            }
            return
        }
        val body = response.body()
        if (response.isSuccessful && body != null) {
            _uiState.update {
                it.copy(
                    loadingState = LoadingState.Success,
                    itemList = body
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    loadingState = LoadingState.Error("Failed to download data. Error code ${response.code()}")
                )
            }
        }
    }
}

@Serializable
data class Item(
    val id: Int?,
    val listId: Int?,
    val name: String?
)

sealed class LoadingState {
    data object Loading : LoadingState()
    data class Error(val message: String) : LoadingState()
    data object Success : LoadingState()
}

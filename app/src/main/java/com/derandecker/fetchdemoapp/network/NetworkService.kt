package com.derandecker.fetchdemoapp.network

import com.derandecker.fetchdemoapp.ui.viewmodel.Item
import retrofit2.Response
import retrofit2.http.GET

interface NetworkService {
    @GET("hiring.json")
    suspend fun getItems(): Response<List<Item>>
}

package com.marina.ruiz.globetrotting.data.network

import com.marina.ruiz.globetrotting.data.network.model.rickAndMortyApi.CharacterResponse
import com.marina.ruiz.globetrotting.data.network.model.rickAndMortyApi.CharacterListResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyApi {
    @GET("api/character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): CharacterResponse

    @GET("api/character")
    suspend fun getAll(): CharacterListResponse
}

class RickAndMortyApiService {
    // Instantiate retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    // Create API
    val api: RickAndMortyApi = retrofit.create(RickAndMortyApi::class.java)
}
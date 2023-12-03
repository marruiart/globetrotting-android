package com.marina.ruiz.globetrotting.data.network.rickAndMortyApi

import com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.model.CharacterListResponse
import com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.model.CharacterResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

interface RickAndMortyApi {
    @GET("api/character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): CharacterResponse

    @GET("api/character")
    suspend fun getAll(): CharacterListResponse
}

@Singleton
class RickAndMortyApiService @Inject constructor() {
    // Instantiate retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create API
    val api: RickAndMortyApi = retrofit.create(RickAndMortyApi::class.java)
}
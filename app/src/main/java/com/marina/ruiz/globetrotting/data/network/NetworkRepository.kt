package com.marina.ruiz.globetrotting.data.network

import com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.RickAndMortyApiService
import com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.model.CharacterApiModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    private val service: RickAndMortyApiService
) {

    suspend fun getAllCharacters(): List<CharacterApiModel> {
        val response = service.api.getAll()
        return response.results.map {
            it.asApiModel()
        }
    }
}
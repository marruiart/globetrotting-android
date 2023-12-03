package com.marina.ruiz.globetrotting.data.network

import com.marina.ruiz.globetrotting.data.network.model.rickAndMortyApi.CharacterApiModel

class NetworkRepository(
    private val service: RickAndMortyApiService
) {

    suspend fun getAllCharacters(): List<CharacterApiModel> {
        val response = service.api.getAll()
        return response.results.map {
            it.asApiModel()
        }
    }
}
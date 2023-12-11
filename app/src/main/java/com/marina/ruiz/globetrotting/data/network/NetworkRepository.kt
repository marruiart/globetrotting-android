package com.marina.ruiz.globetrotting.data.network

import com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.RickAndMortyApiService
import com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.model.CharacterApiModel
import com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.model.LocationApiModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    private val service: RickAndMortyApiService,
) {

    suspend fun getAllCharacters(): List<CharacterApiModel> {
        val response = service.api.getAllCharacters()
        return response.results.map {
            it.asApiModel()
        }
    }

    suspend fun getAllLocations(): List<LocationApiModel> {
        val response = service.api.getAllLocations()
        return response.results.map { result ->
            result.asApiModel()
        }
    }
}
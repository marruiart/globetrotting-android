package com.marina.ruiz.globetrotting.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.marina.ruiz.globetrotting.data.network.model.CharacterApiModel
import com.marina.ruiz.globetrotting.data.network.model.CharacterDetailResponse
import com.marina.ruiz.globetrotting.data.network.model.CharacterListApiModel
import com.marina.ruiz.globetrotting.data.network.model.CharacterListResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyApi {
    @GET("api/character/{id}")
    suspend fun fetchCharacter(@Path("id") id: Int): CharacterDetailResponse

    @GET("api/character")
    suspend fun fetchCharactersList(): CharacterListResponse
}

class CharactersRepository private constructor(private val api: RickAndMortyApi) {

    private var _characters = MutableLiveData<CharacterListApiModel>()
    val characters: LiveData<CharacterListApiModel>
        get() = _characters

    companion object {
        private var _INSTANCE: CharactersRepository? = null
        fun getInstance(): CharactersRepository {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://rickandmortyapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val rickAndMortyApi = retrofit.create(RickAndMortyApi::class.java)
            _INSTANCE = _INSTANCE ?: CharactersRepository(rickAndMortyApi)
            return _INSTANCE!!
        }
    }

    private fun mapCharacter(characterResponse: CharacterDetailResponse): CharacterApiModel =
        CharacterApiModel(
            characterResponse.id,
            characterResponse.name,
            characterResponse.status,
            characterResponse.species,
            characterResponse.type,
            characterResponse.gender,
            characterResponse.image
        )

    suspend fun fetchList() {
        val characterListResponse = api.fetchCharactersList()
        Log.d("DEBUG", characterListResponse.toString())
        val characterList = characterListResponse.results.map {
            mapCharacter(it)
        }
        val characterListApiModel = CharacterListApiModel(characterList)
        _characters.value = characterListApiModel
    }
}
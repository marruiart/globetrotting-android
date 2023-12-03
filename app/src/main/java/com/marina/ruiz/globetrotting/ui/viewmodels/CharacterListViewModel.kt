package com.marina.ruiz.globetrotting.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.data.repository.model.Traveler
import com.marina.ruiz.globetrotting.data.network.NetworkRepository
import com.marina.ruiz.globetrotting.data.network.model.rickAndMortyApi.CharacterApiModel
import com.marina.ruiz.globetrotting.data.network.model.rickAndMortyApi.CharacterListApiModel
import kotlinx.coroutines.launch

class CharacterListViewModel : ViewModel() {
    private val repository = NetworkRepository.getInstance()

    private val _characters = MutableLiveData<List<Traveler>>()
    val characters: LiveData<List<Traveler>>
        get() = _characters

    private fun mapCharacter(c: CharacterApiModel): Traveler = Traveler(
        c.id, c.name, c.species, c.status, c.type, c.gender, c.image
    )

    private val observer = Observer<CharacterListApiModel> {
        _characters.value = it.list.map {
            mapCharacter(it)
        }
    }

    init {
        fetchAll()
    }

    fun fetchAll() {
        viewModelScope.launch {
            // scope to execute suspendable functions
            repository.characters.observeForever(observer)
            viewModelScope.launch {
                repository.fetchList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.characters.removeObserver(observer)
    }

}
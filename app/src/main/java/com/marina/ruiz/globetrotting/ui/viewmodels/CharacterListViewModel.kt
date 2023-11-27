package com.marina.ruiz.globetrotting.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.data.repository.model.Character
import com.marina.ruiz.globetrotting.data.network.CharactersRepository
import com.marina.ruiz.globetrotting.data.network.model.CharacterApiModel
import com.marina.ruiz.globetrotting.data.network.model.CharacterListApiModel
import kotlinx.coroutines.launch

class CharacterListViewModel : ViewModel() {
    private val repository = CharactersRepository.getInstance()

    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>>
        get() = _characters

    private fun mapCharacter(c: CharacterApiModel): Character = Character(
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
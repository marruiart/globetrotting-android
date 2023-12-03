package com.marina.ruiz.globetrotting.data.repository

import com.marina.ruiz.globetrotting.data.local.LocalRepository
import com.marina.ruiz.globetrotting.data.local.asListTraveler
import com.marina.ruiz.globetrotting.data.network.NetworkRepository
import com.marina.ruiz.globetrotting.data.network.model.rickAndMortyApi.asEntityModelList
import com.marina.ruiz.globetrotting.data.repository.model.Traveler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobetrottingRepository @Inject constructor(
    private val localRepository: LocalRepository,
    private val networkRepository: NetworkRepository
) {
    // Expose data operations
    val travelers: Flow<List<Traveler>>
        get() {
            // offline first
            return localRepository.travelers.map {
                it.asListTraveler()
            }
        }

    suspend fun refreshList() = withContext(Dispatchers.IO) {
        // SCOPE: suspendable code -> executed asynchronously in a coroutine.
        // Dispatchers.IO is a special thread for network operations
        val characterApiModelList = networkRepository.getAllCharacters()
        localRepository.insert(characterApiModelList.asEntityModelList())
    }
}
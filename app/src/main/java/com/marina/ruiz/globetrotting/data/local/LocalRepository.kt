package com.marina.ruiz.globetrotting.data.local

import android.util.Log
import androidx.annotation.WorkerThread
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(
    private val destinationDao: DestinationDao,
    private val userDao: UserDao
) {

    companion object {
        private const val TAG = "GLOB_DEBUG LOCAL_REPO"
    }

    // DESTINATION
    var destinations: Flow<List<DestinationEntity>> = destinationDao.getAllDestinations()

    @WorkerThread
    suspend fun insertDestinations(listDestinationEntity: List<DestinationEntity>) =
        destinationDao.createDestinations(listDestinationEntity)

    @WorkerThread
    suspend fun insertDestination(destinationEntity: DestinationEntity) =
        destinationDao.createDestination(destinationEntity)

    @WorkerThread
    suspend fun updateDestination(destination: DestinationEntity): Flow<Destination> {
        destinationDao.updateDestination(destination)
        destinations = destinationDao.getAllDestinations()
        return flowOf(destination.asDestination())
    }

    @WorkerThread
    suspend fun deleteDestination(destination: DestinationEntity) {
        destinationDao.deleteDestination(destination)
        destinations = destinationDao.getAllDestinations()
    }

    // USER
    var localUser: Flow<UserEntity?> = userDao.getUser()

    @WorkerThread
    suspend fun insertUser(userEntity: UserEntity) {
        userDao.createUser(userEntity)
        localUser = userDao.getUser()
        Log.d(TAG, "Insert user")
    }

    @WorkerThread
    suspend fun deleteUser() {
        userDao.deleteUser()
        localUser = flowOf(null)
    }
}
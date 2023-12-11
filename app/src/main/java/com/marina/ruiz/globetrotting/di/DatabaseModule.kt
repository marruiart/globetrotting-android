package com.marina.ruiz.globetrotting.di

import android.content.Context
import com.marina.ruiz.globetrotting.data.local.DestinationDao
import com.marina.ruiz.globetrotting.data.local.GlobetrottingDb
import com.marina.ruiz.globetrotting.data.local.TravelerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): GlobetrottingDb {
        return GlobetrottingDb.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideTravelerDao(database: GlobetrottingDb): TravelerDao {
        return database.travelerDao()
    }

    @Singleton
    @Provides
    fun provideDestinationDao(database: GlobetrottingDb): DestinationDao {
        return database.destinationDao()
    }
}
package com.marina.ruiz.globetrotting.di

import android.content.Context
import com.marina.ruiz.globetrotting.data.local.agent.AgentDao
import com.marina.ruiz.globetrotting.data.local.booking.BookingDao
import com.marina.ruiz.globetrotting.data.local.destination.DestinationDao
import com.marina.ruiz.globetrotting.data.local.GlobetrottingDb
import com.marina.ruiz.globetrotting.data.local.user.UserDao
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
    fun provideDestinationDao(database: GlobetrottingDb): DestinationDao {
        return database.destinationDao()
    }

    @Singleton
    @Provides
    fun provideBookingDao(database: GlobetrottingDb): BookingDao {
        return database.bookingDao()
    }

    @Singleton
    @Provides
    fun provideUserDao(database: GlobetrottingDb): UserDao {
        return database.userDao()
    }

    @Singleton
    @Provides
    fun provideAgentDao(database: GlobetrottingDb): AgentDao {
        return database.agentDao()
    }
}
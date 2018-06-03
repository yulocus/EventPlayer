package com.yulocus.eventplayer.repository

object RepositoryProvider {
    fun provideEventRepository(): EventRepository = EventRepository()
}
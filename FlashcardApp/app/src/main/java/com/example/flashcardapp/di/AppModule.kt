package com.example.flashcardapp.di

import android.app.Application
import androidx.room.Room
import com.example.flashcardapp.data.local.AppDatabase
import com.example.flashcardapp.data.local.FlashcardDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "flashcard_db"
        ).build()
    }

    @Provides
    fun provideDao(db: AppDatabase): FlashcardDao {
        return db.flashcardDao()
    }
}
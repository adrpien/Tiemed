package com.adrpien.tiemed.depency_injection

import android.app.Application
import androidx.room.Room
import com.adrpien.tiemed.data.TiemedRepositoryImplementation
import com.adrpien.tiemed.data.local.TiemedDao
import com.adrpien.tiemed.data.local.TiemedDatabase
import com.adrpien.tiemed.data.remote.FirebaseApi
import com.adrpien.tiemed.domain.repository.TiemedRepository
import com.bumptech.glide.annotation.GlideModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Provides
    @Singleton
    fun ProvideFirebaseStorage(): FirebaseStorage{
       return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun ProvideFirebaseAuthentication(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun ProvideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun ProvideFirebaseApi(): FirebaseApi {
        return FirebaseApi()
    }

    @Provides
    @Singleton
    fun ProvideTiemedDatabase(app: Application): TiemedDatabase {
        return Room.databaseBuilder(
            app,
            TiemedDatabase::class.java,
            "tiemed_db"
        ).build()
    }

    @Provides
    @Singleton
    fun ProvideRepository(tiemedDatabase: TiemedDatabase, firebaseApi: FirebaseApi): TiemedRepository {
        return TiemedRepositoryImplementation(
            tiemedDatabase.tiemedDao,
            firebaseApi
        )
    }




}
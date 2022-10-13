package com.example.lab8_plataformas.di

import android.content.Context
import androidx.room.Room
import com.example.lab8_plataformas.Data.datasource.api.RickMortyAPI
import com.example.lab8_plataformas.Data.datasource.local.Database
import com.example.lab8_plataformas.Data.datasource.local.ResultDao
import com.example.lab8_plataformas.Data.datasource.util.Constants.Companion.RICKMORT_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(Singleton::class)
object AppModule {
    @Provides
    @Singleton
    fun provideLogginInterceptor():HttpLoggingInterceptor{
        val loggin = HttpLoggingInterceptor()
        loggin.setLevel(HttpLoggingInterceptor.Level.BODY)
        return loggin
    }

    @Provides
    @Singleton
    fun provideHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(client: OkHttpClient): RickMortyAPI{
        return Retrofit.Builder()
            .baseUrl(RICKMORT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickMortyAPI:: class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context):Database{
        return Room.databaseBuilder(
            context,
            Database::class.java,
            "labDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCharacterDao(database: Database): ResultDao{
        return database.dataCharacterDao()
    }
}
package com.example.lab8_plataformas.Data.datasource.api

import com.example.lab8_plataformas.Data.datasource.model.AllAssetsResponse
import com.example.lab8_plataformas.Data.datasource.model.OneCharacter
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RickMortyAPI {

    @GET("/api/character/")
    suspend fun getCharacter(): AllAssetsResponse

    @GET("/api/character/{id}")
    suspend fun getSingleCharacter(
        @Path("id") id: String
    ): OneCharacter

}
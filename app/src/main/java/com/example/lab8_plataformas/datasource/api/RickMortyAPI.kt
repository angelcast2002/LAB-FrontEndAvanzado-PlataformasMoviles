package com.example.lab8_plataformas.datasource.api

import com.example.lab8_plataformas.datasource.model.AllAssetsResponse
import com.example.lab8_plataformas.datasource.model.OneCharacter
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RickMortyAPI {

    @GET("/api/character/")
    fun getCharacter(

    ): Call<AllAssetsResponse>

    @GET("/api/character/{id}")
    fun getSingleCharacter(
        @Path("id") id: String
    ): Call<OneCharacter>

}
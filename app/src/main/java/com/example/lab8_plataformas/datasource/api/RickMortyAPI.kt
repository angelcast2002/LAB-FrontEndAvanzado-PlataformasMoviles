package com.example.lab8_plataformas.datasource.api

import com.example.lab8_plataformas.datasource.model.AllAssetsResponse
import com.example.lab8_plataformas.datasource.model.OneCharacter
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickMortyAPI {

    @GET("/api/character/{listaIdString}")
    //1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25
    fun getCharacter(
        @Path("listaIdString") listaIdString: String
    ): Call<AllAssetsResponse>

    @GET("/api/character/{id}")
    fun getSingleCharacter(
        @Path("id") id: String
    ): Call<OneCharacter>

}
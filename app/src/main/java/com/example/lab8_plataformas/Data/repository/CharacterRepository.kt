package com.example.lab8_plataformas.Data.repository

import com.example.lab8_plataformas.Data.datasource.model.dataCharacters
import com.example.lab8_plataformas.Data.datasource.util.Resource


interface CharacterRepository {

    suspend fun getAllCharacters(): Resource<List<dataCharacters>>
    suspend fun deleteAllCharacters(): Resource<Unit>
    suspend fun getCharacter(id: Int): Resource<dataCharacters?>
    suspend fun updateCharacter(character: dataCharacters): Resource<Unit>
    suspend fun deleteCharacter(id: Int): Resource<Unit>
}
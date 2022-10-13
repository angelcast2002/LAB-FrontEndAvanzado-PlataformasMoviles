package com.example.lab8_plataformas.Data.repository

import com.example.lab8_plataformas.Data.datasource.model.dataCharacters
import com.example.lab8_plataformas.Data.datasource.util.DataState
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    fun getAllCharacter(): Flow<DataState<List<dataCharacters>>>
    fun deleteAllCharacter(): Flow<DataState<Int>>
    fun getCharacter(id: String): Flow<DataState<dataCharacters?>>
    fun updateCharacter(character: dataCharacters): Flow<DataState<Int>>
    fun deleteCharacter(id: String): Flow<DataState<Int>>
}
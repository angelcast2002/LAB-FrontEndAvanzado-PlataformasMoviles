package com.example.lab8_plataformas.Data.repository

import com.example.lab8_plataformas.Data.datasource.api.RickMortyAPI
import com.example.lab8_plataformas.Data.datasource.local.ResultDao
import com.example.lab8_plataformas.Data.datasource.model.dataCharacters
import com.example.lab8_plataformas.Data.datasource.model.mapToModel
import com.example.lab8_plataformas.Data.datasource.util.DataState
import com.example.lab8_plataformas.Data.datasource.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class CharacterRepositoryImpl(
    private val characterDao: ResultDao,
    private val api: RickMortyAPI
): CharacterRepository {
    override suspend fun getAllCharacters(): Resource<List<dataCharacters>> {
        val localCharacter = characterDao.getUsers()
        return if (localCharacter.isEmpty()){
            try {
                val remoteCharacters = api.getCharacter().results
                val charactersToStore = remoteCharacters.map { dto -> dto.mapToModel() }
                characterDao.insertAll(charactersToStore)
                Resource.Success(charactersToStore)
            } catch (e: Exception){
                Resource.Error(e.message ?: "")
            }
        } else {
            Resource.Success(localCharacter)
        }
    }

    override suspend fun deleteAllCharacters(): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getCharacter(id: Int): Resource<dataCharacters?> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCharacter(character: dataCharacters): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCharacter(id: Int): Resource<Unit> {
        TODO("Not yet implemented")
    }

}
package com.example.lab8_plataformas.Data.repository

import com.example.lab8_plataformas.Data.datasource.api.RickMortyAPI
import com.example.lab8_plataformas.Data.datasource.local.ResultDao
import com.example.lab8_plataformas.Data.datasource.model.dataCharacters
import com.example.lab8_plataformas.Data.datasource.model.mapToModel
import com.example.lab8_plataformas.Data.datasource.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class CharacterRepositoryImpl(
    private val characterDao: ResultDao,
    private val api: RickMortyAPI
): CharacterRepository {
    override fun getAllCharacter(): Flow<DataState<List<dataCharacters>>> = flow {
        emit(DataState.Loading)
        val localCharacter = characterDao.getUsers()
        if (localCharacter.isEmpty()){
            try {
                val remoteCharacter = api.getCharacter().results
                val charactersToStore = remoteCharacter.map{dto -> dto.mapToModel()}
                characterDao.insertAll(charactersToStore)
                emit(DataState.Success(charactersToStore))
            }
            catch (e: Exception){
                emit(DataState.Success(localCharacter))
            }
        }
        else{
            emit(DataState.Success(localCharacter))
        }
    }

    override fun deleteAllCharacter(): Flow<DataState<Int>> {
        TODO("Not yet implemented")
    }

    override fun getCharacter(id: String): Flow<DataState<dataCharacters?>> {
        TODO("Not yet implemented")
    }

    override fun updateCharacter(character: dataCharacters): Flow<DataState<Int>> {
        TODO("Not yet implemented")
    }

    override fun deleteCharacter(id: String): Flow<DataState<Int>> {
        TODO("Not yet implemented")
    }
}
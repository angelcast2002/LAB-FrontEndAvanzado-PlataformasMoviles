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
                emit(DataState.Error(e))
            }
        }
        else{
            emit(DataState.Success(localCharacter))
        }
    }

    override fun deleteAllCharacter(): Flow<DataState<Int>> = flow{

        emit(DataState.Loading)
        try {
            val numberOfCharacterDeleted = characterDao.deleteAll()
            emit(DataState.Success(numberOfCharacterDeleted))
        }catch (e: Exception){
            emit(DataState.Error(e))
        }

    }

    override fun getCharacter(id: String): Flow<DataState<dataCharacters?>> = flow{

        emit(DataState.Loading)
        try {
            val localCharacter = characterDao.getUserById(id.toInt())
            emit(DataState.Success(localCharacter))
        }
        catch (e: Exception){
            emit(DataState.Error(e))
        }
    }

    override fun updateCharacter(character: dataCharacters): Flow<DataState<Int>> = flow {

        emit(DataState.Loading)
        try {
            characterDao.update(character)
            emit(DataState.Success(character.id))
        }catch (e: Exception){
            emit(DataState.Error(e))
        }
    }

    override fun deleteCharacter(id: String): Flow<DataState<Int>> = flow{

        emit(DataState.Loading)
        try {
            characterDao.delete(characterDao.getUserById(id.toInt()))
            emit(DataState.Success(id.toInt()))
        }catch (e: Exception){
            emit(DataState.Error(e))
        }
    }
}
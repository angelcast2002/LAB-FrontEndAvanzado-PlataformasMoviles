package com.example.lab8_plataformas.Data.datasource.local

import androidx.room.*
import com.example.lab8_plataformas.Data.datasource.model.dataCharacters

@Dao
interface ResultDao {

    @Query("SELECT * FROM dataCharacters")
    suspend fun getUsers(): List<dataCharacters>

    @Query("SELECT * FROM dataCharacters WHERE id = :id")
    suspend fun getUserById(id: Int): dataCharacters

    @Update
    suspend fun update(user: dataCharacters)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: dataCharacters)

    @Delete
    suspend fun delete(user: dataCharacters): Int

    @Query("DELETE FROM dataCharacters")
    suspend fun deleteAll(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(user: List<dataCharacters>)


}
package com.example.lab8_plataformas.Data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lab8_plataformas.Data.datasource.model.dataCharacters

@Database(entities = [dataCharacters::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun dataCharacterDao(): ResultDao
}
package com.example.lab8_plataformas.Data.datasource.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class dataCharacters(
    @PrimaryKey(autoGenerate = false) val id: Int,
    var episode: Int?,
    var gender: String?,
    val image: String?,
    var name: String?,
    var origin: String?,
    var species: String?,
    var status: String?
)

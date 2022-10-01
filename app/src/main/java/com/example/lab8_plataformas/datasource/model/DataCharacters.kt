package com.example.lab8_plataformas.datasource.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class dataCharacters(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val episode: Int?,
    val gender: String?,
    val image: String?,
    val name: String?,
    val origin: String?,
    val species: String?,
    val status: String?
)

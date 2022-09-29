package com.example.lab8_plataformas.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lab8_plataformas.datasource.model.LocationX
import com.example.lab8_plataformas.datasource.model.OriginOneCharacter

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val created: String,
    val episode: List<String>,
    val gender: String,
    val image: String,
    val location: LocationX,
    val name: String,
    val origin: OriginOneCharacter,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)

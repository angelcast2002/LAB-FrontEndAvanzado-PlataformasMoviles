package com.example.lab8_plataformas.Data.datasource.model

data class OneCharacter(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: LocationX,
    val name: String,
    val origin: OriginOneCharacter,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)
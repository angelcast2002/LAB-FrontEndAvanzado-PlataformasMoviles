package com.example.lab8_plataformas.Data.datasource.model

data class Result(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: Location,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)

fun Result.mapToModel(): dataCharacters = dataCharacters(
    name = name,
    id = id.toInt(),
    image = image,
    status = status,
    gender = gender,
    origin = origin.name,
    species = species,
    episode = episode.size
)
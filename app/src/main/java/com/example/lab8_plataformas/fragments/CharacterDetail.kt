package com.example.lab8_plataformas.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.example.lab8_plataformas.R
import com.example.lab8_plataformas.datasource.api.RetrofitInstance
import com.example.lab8_plataformas.datasource.model.OneCharacter
import com.example.lab8_plataformas.datasource.model.Result
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetail : Fragment(R.layout.character_detail){

    private lateinit var characterImage : ImageView
    private lateinit var characterName : TextView
    private lateinit var characterSpecies : TextView
    private lateinit var characterStatus : TextView
    private lateinit var characterGender : TextView
    private lateinit var characterID : String
    private lateinit var resultadoLlamadaAPI : OneCharacter
    private lateinit var characterOrigin : TextView
    private lateinit var characterEpisodeAppearances: TextView
    private lateinit var toolbar: MaterialToolbar

    private val args: CharacterDetailArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        characterImage = view.findViewById(R.id.imageView_characterDetail_fragment)
        characterName = view.findViewById(R.id.nameCharacter_characterDetail_fragment)
        characterSpecies = view.findViewById(R.id.textRace_characterDetail_fragment)
        characterStatus = view.findViewById(R.id.textAliveDeath_characterDetail_fragment)
        characterGender = view.findViewById(R.id.textMaleFemale_characterDetail_fragment)
        characterOrigin = view.findViewById(R.id.textOriginData_characterDetail_fragment)
        characterEpisodeAppearances = view.findViewById(R.id.textEpisodeData_characterDetail_fragment)
        toolbar = view.findViewById(R.id.toolbar_ToolbarActivity_characterDetail_characterDetail)
        setID()
        apiRequest()
        setToolbar()
    }

    private fun setID() {
        characterID = args.characterID
    }

    private fun setImage() {
        characterImage.load(resultadoLlamadaAPI.image) {
            transformations(CircleCropTransformation())
            diskCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
            error(R.drawable.ic_error)
            placeholder(R.drawable.ic_download)
        }
    }

    private fun setInfo() {
        characterName.text = resultadoLlamadaAPI.name
        characterSpecies.text = resultadoLlamadaAPI.species
        characterStatus.text = resultadoLlamadaAPI.status
        characterGender.text = resultadoLlamadaAPI.gender
        characterOrigin.text = resultadoLlamadaAPI.origin.name
        characterEpisodeAppearances.text = resultadoLlamadaAPI.episode.size.toString()
    }

    private fun apiRequest() {

        RetrofitInstance.api.getSingleCharacter(characterID).enqueue(object : Callback<OneCharacter>{
            override fun onResponse(call: Call<OneCharacter>, response: Response<OneCharacter>) {
                if (response.isSuccessful && response.body() != null){
                    resultadoLlamadaAPI = response.body()!!
                    setImage()
                    setInfo()
                }
            }

            override fun onFailure(call: Call<OneCharacter>, t: Throwable) {
                println("Error")
            }

        })
    }

    private fun setToolbar() {
        val navController = findNavController()
        val appbarConfig = AppBarConfiguration(navController.graph)

        toolbar.setupWithNavController(navController, appbarConfig)
    }
}
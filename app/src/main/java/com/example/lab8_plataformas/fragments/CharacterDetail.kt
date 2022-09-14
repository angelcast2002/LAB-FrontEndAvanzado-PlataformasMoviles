package com.example.lab8_plataformas.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.example.lab8_plataformas.R
import com.example.lab8_plataformas.datasource.api.RetrofitInstance
import com.example.lab8_plataformas.datasource.model.AllAssetsResponse
import com.example.lab8_plataformas.datasource.model.OneCharacter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetail : Fragment(R.layout.character_detail){

    private lateinit var characterImage : ImageView
    private lateinit var characterName : TextView
    private lateinit var characterSpecies : TextView
    private lateinit var characterStatus : TextView
    private lateinit var characterGender : TextView
    private lateinit var id: String

    private val args: CharacterDetailArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        characterImage = view.findViewById(R.id.imageView_characterDetail_fragment)
        characterName = view.findViewById(R.id.nameCharacter_characterDetail_fragment)
        characterSpecies = view.findViewById(R.id.textRace_characterDetail_fragment)
        characterStatus = view.findViewById(R.id.textAliveDeath_characterDetail_fragment)
        characterGender = view.findViewById(R.id.textMaleFemale_characterDetail_fragment)

        setImage()
        setInfo()
        apiRequest()
    }

    private fun setImage() {
        characterImage.load(args.characterInfo.image) {
            transformations(CircleCropTransformation())
            diskCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
            error(R.drawable.ic_error)
            placeholder(R.drawable.ic_download)
        }
    }

    private fun setInfo() {
        characterName.text = args.characterInfo.name
        characterSpecies.text = args.characterInfo.species
        characterStatus.text = args.characterInfo.status
        characterGender.text = args.characterInfo.gender
    }

    private fun apiRequest() {

        RetrofitInstance.api.getSingleCharacter("1").enqueue(object : Callback<OneCharacter>{
            override fun onResponse(call: Call<OneCharacter>, response: Response<OneCharacter>) {
                if (response.isSuccessful){
                    println(response.body())
                }
            }

            override fun onFailure(call: Call<OneCharacter>, t: Throwable) {
                println("Error")
            }

        })

    }
}
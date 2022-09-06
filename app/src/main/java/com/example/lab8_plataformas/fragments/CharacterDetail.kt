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

class CharacterDetail : Fragment(R.layout.character_detail){

    private lateinit var characterImage : ImageView
    private lateinit var characterName : TextView
    private lateinit var characterSpecies : TextView
    private lateinit var characterStatus : TextView
    private lateinit var characterGender : TextView

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
}
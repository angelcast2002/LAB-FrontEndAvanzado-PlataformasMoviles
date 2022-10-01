package com.example.lab8_plataformas.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.example.lab8_plataformas.R
import com.example.lab8_plataformas.datasource.api.RetrofitInstance
import com.example.lab8_plataformas.datasource.local.Database
import com.example.lab8_plataformas.datasource.model.OneCharacter
import com.example.lab8_plataformas.datasource.model.Result
import com.example.lab8_plataformas.datasource.model.dataCharacters
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetail : Fragment(R.layout.character_detail){

    private lateinit var characterImage : ImageView
    private lateinit var characterName : EditText
    private lateinit var characterSpecies : EditText
    private lateinit var characterStatus : EditText
    private lateinit var characterGender : EditText
    private lateinit var resultadoLlamadaAPI : OneCharacter
    private lateinit var characterOrigin : EditText
    private lateinit var characterEpisodeAppearances: EditText
    private lateinit var toolbar: MaterialToolbar
    private lateinit var database: Database

    private val args: CharacterDetailArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        characterImage = view.findViewById(R.id.imageView_characterDetail_fragment)
        characterName = view.findViewById(R.id.nameCharacter_characterDetail_fragment_editText)
        characterSpecies = view.findViewById(R.id.textSpecies_characterDetail_fragment_editText)
        characterStatus = view.findViewById(R.id.textStatus_characterDetail_fragment_editText)
        characterGender = view.findViewById(R.id.textGender_characterDetail_fragment_editText)
        characterOrigin = view.findViewById(R.id.textOrigin_characterDetail_fragment_fragment_editText)
        characterEpisodeAppearances = view.findViewById(R.id.textEpisode_characterDetail_fragment_editText)
        toolbar = view.findViewById(R.id.toolbar_ToolbarActivity_characterDetail_characterDetail)

        database = Room.databaseBuilder(
            requireContext(),
            Database::class.java,
            "dataBaseCharacters"
        ).build()

        setToolbar()
        fetchUser()
    }


    private fun setToolbar() {
        val navController = findNavController()
        val appbarConfig = AppBarConfiguration(navController.graph)

        toolbar.setupWithNavController(navController, appbarConfig)
    }

    private fun fetchUser() {
        CoroutineScope(Dispatchers.IO).launch {
            val user = database.dataCharacterDao().getUserById(args.characterID)
            CoroutineScope(Dispatchers.Main).launch {
                characterImage.load(user.image) {
                    transformations(CircleCropTransformation())
                    diskCachePolicy(CachePolicy.ENABLED)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    error(R.drawable.ic_error)
                    placeholder(R.drawable.ic_download)
                }
                characterName.setText(user.name)
                characterSpecies.setText(user.species)
                characterStatus.setText(user.status)
                characterGender.setText(user.gender)
                characterOrigin.setText(user.origin)
                characterEpisodeAppearances.setText(user.episode.toString())
            }
        }
    }
    private fun setListeners() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.menu_item_AZ -> {

                    true
                }

                R.id.menu_item_ZA -> {
                    
                    true
                }
                R.id.menu_item_cerrarSesion -> {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.apply {
                        setTitle(getString(R.string.text_Advertencia))
                        setMessage(getString(R.string.text_mensaje_cerrarSesion))
                        setPositiveButton(getString(R.string.text_Eliminar)
                        ) { _, _ ->
                            delete()
                        }
                        setNegativeButton(getString(R.string.text_Cancelar)) { _, _ -> }
                        show()
                    }

                    true
                }
                R.id.menu_item_sync -> {
                    data.clear()
                    apiRequest()
                    true
                }
                else -> true
            }
        }
    }
}
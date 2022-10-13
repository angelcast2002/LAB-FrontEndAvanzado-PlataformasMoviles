package com.example.lab8_plataformas.Ui.fragments.character_detail

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
import com.example.lab8_plataformas.Data.datasource.api.RetrofitInstance
import com.example.lab8_plataformas.Data.datasource.local.Database
import com.example.lab8_plataformas.Data.datasource.model.OneCharacter
import com.example.lab8_plataformas.Data.datasource.model.dataCharacters
import com.google.android.material.appbar.MaterialToolbar
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
    private lateinit var characterOrigin : EditText
    private lateinit var characterEpisodeAppearances: EditText
    private lateinit var toolbar: MaterialToolbar
    private lateinit var database: Database
    private lateinit var user: dataCharacters
    private lateinit var resultadoLlamadaAPI : OneCharacter
    private lateinit var buttonGuardar: Button

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
        buttonGuardar = view.findViewById(R.id.bt_buttonGuardar_characterDetail)

        database = Room.databaseBuilder(
            requireContext(),
            Database::class.java,
            "dataBaseCharacters"
        ).build()

        setListeners()
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
            user = database.dataCharacterDao().getUserById(args.characterID)
            CoroutineScope(Dispatchers.Main).launch {
                setChanges()
            }
        }
    }

    private fun setListeners() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.menu_item_delete -> {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.apply {
                        setTitle(getString(R.string.text_Advertencia))
                        setMessage(getString(R.string.text_mensaje_deleteCharacter))
                        setPositiveButton(getString(R.string.text_Eliminar)
                        ) { _, _ ->
                            delete()
                        }
                        setNegativeButton(getString(R.string.text_Cancelar)) { _, _ -> }
                        show()
                    }
                    true
                }

                R.id.menu_item_sync_characterDetail -> {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.apply {
                        setTitle(getString(R.string.text_Advertencia))
                        setMessage(getString(R.string.text_mensaje_syncCharacter))
                        setPositiveButton(getString(R.string.text_Eliminar)
                        ) { _, _ ->
                            apiRequest()
                        }
                        setNegativeButton(getString(R.string.text_Cancelar)) { _, _ -> }
                        show()

                    }
                    true
                }
                else -> true
            }
        }

        buttonGuardar.setOnClickListener{
            getDataFromInputText()
        }
    }

    private fun saveNewData() {
        CoroutineScope(Dispatchers.IO).launch {
            database.dataCharacterDao().update(user)
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(requireContext(), getString(R.string.text_mensaje_saveNewData), Toast.LENGTH_SHORT).show()
            }
        }
        //Toast.makeText(requireContext(), getString(R.string.text_mensaje_saveNewData), Toast.LENGTH_SHORT).show()
    }

    private fun getDataFromInputText() {
        user.name = characterName.text.toString()
        user.species = characterSpecies.text.toString()
        user.gender = characterGender.text.toString()
        user.origin = characterOrigin.text.toString()
        user.status = characterStatus.text.toString()
        user.episode = characterEpisodeAppearances.text.toString().toIntOrNull()
        saveNewData()
    }

    private fun apiRequest() {
        RetrofitInstance.api.getSingleCharacter(args.characterID.toString()).enqueue(object : Callback<OneCharacter> {
            override fun onResponse(call: Call<OneCharacter>, response: Response<OneCharacter>) {
                if (response.isSuccessful && response.body() != null) {
                    resultadoLlamadaAPI = response.body()!!
                    setChangesFromApi()
                }
            }

            override fun onFailure(call: Call<OneCharacter>, t: Throwable) {
                Toast.makeText(requireContext(), getString(R.string.text_mensaje_noInternet), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun delete() {
        CoroutineScope(Dispatchers.IO).launch {
            val rowsDeleted = database.dataCharacterDao().delete(user)
            CoroutineScope(Dispatchers.Main).launch {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun setChanges(){
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

    private fun setChangesFromApi(){
        characterImage.load(resultadoLlamadaAPI.image) {
            transformations(CircleCropTransformation())
            diskCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
            error(R.drawable.ic_error)
            placeholder(R.drawable.ic_download)
        }
        characterName.setText(resultadoLlamadaAPI.name)
        characterSpecies.setText(resultadoLlamadaAPI.species)
        characterStatus.setText(resultadoLlamadaAPI.status)
        characterGender.setText(resultadoLlamadaAPI.gender)
        characterOrigin.setText(resultadoLlamadaAPI.origin.name)
        characterEpisodeAppearances.setText(resultadoLlamadaAPI.episode.size.toString())
        getDataFromInputText()
    }
}
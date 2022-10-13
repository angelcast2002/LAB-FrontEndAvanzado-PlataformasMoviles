package com.example.lab8_plataformas.Ui.fragments.character_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.lab8_plataformas.R
import com.example.lab8_plataformas.Data.dataStore.DataStore.dataStore
import com.example.lab8_plataformas.Data.datasource.api.RetrofitInstance
import com.example.lab8_plataformas.Data.datasource.local.Database
import com.example.lab8_plataformas.Data.datasource.model.AllAssetsResponse
import com.example.lab8_plataformas.Data.datasource.model.dataCharacters
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceListFragment : Fragment(R.layout.fragment_place_list), PlaceAdapter.PlaceListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var placesList: MutableList<dataCharacters>
    private  var data : MutableList<dataCharacters> = mutableListOf()
    private lateinit var toolbar: MaterialToolbar
    private lateinit var database: Database
    private lateinit var progressBar: ProgressBar



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView = view.findViewById(R.id.recycler_recyclerActivity)
        toolbar = view.findViewById(R.id.toolbar_ToolbarActivity_characterDetail_characterList)
        progressBar = view.findViewById(R.id.progress_users)
        data = mutableListOf()

        database = Room.databaseBuilder(
            requireContext(),
            Database::class.java,
            "dataBaseCharacters"
        ).build()

        setListeners()
        setDataFromRoom()
        setupRecycler()

    }

    private fun setDataFromRoom() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        //data.clear()
        CoroutineScope(Dispatchers.IO).launch {
            val users = database.dataCharacterDao().getUsers()
            data.addAll(users)
            CoroutineScope(Dispatchers.Main).launch {
                placesList = data
                checkData()
            }
        }


    }

    private fun checkData() {

        if (data.size != 0){
            notifyDataChange()

        }

        else{
            apiRequest()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun notifyDataChange() {
        recyclerView.adapter!!.notifyDataSetChanged()
        progressBar.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListeners() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.menu_item_AZ -> {
                    placesList.sortBy { character -> character.name }
                    recyclerView.adapter!!.notifyDataSetChanged()
                    true
                }

                R.id.menu_item_ZA -> {
                    placesList.sortByDescending { character -> character.name }
                    recyclerView.adapter!!.notifyDataSetChanged()
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

    private fun setupRecycler() {
        placesList = data
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = PlaceAdapter(placesList, this)
        recyclerView.visibility = View.VISIBLE
    }

    override fun onPlaceClicked(data: dataCharacters, position: Int) {
        requireView().findNavController().navigate(
            PlaceListFragmentDirections.actionPlaceListFragmentToPlaceDetailsFragment(
                characterID = data.id
            )
        )
    }

    private fun apiRequest(){
        RetrofitInstance.api.getCharacter().enqueue(object : Callback<AllAssetsResponse> {
            override fun onResponse(
                call: Call<AllAssetsResponse>,
                response: Response<AllAssetsResponse>
            ) {
                if (response.isSuccessful && response.body() != null){
                    for (user in response.body()!!.results){
                        val character = dataCharacters(
                            name = user.name,
                            species = user.species,
                            id = user.id,
                            status = user.status,
                            gender = user.gender,
                            origin = user.origin.name,
                            episode = user.episode.size,
                            image = user.image
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            database.dataCharacterDao().insert(character)
                        }
                        data.add(character)
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        placesList = data
                        notifyDataChange()
                    }

                }
            }

            override fun onFailure(call: Call<AllAssetsResponse>, t: Throwable) {
                Toast.makeText(requireContext(), getString(R.string.text_mensaje_noInternet), Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun delete() {
        CoroutineScope(Dispatchers.IO).launch {
            saveKeyValue(
                key = getString(R.string.keyInicioSesion)
            )
            database.dataCharacterDao().deleteAll()

        }

        requireView().findNavController().navigate(
            PlaceListFragmentDirections.actionPlaceListFragmentToLoginFragment()
        )
    }

    private suspend fun saveKeyValue(key: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context?.dataStore?.edit { settings ->
            settings.remove(dataStoreKey)
        }
    }

}
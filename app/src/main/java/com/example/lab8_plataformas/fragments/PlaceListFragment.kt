package com.example.lab8_plataformas.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.lab8_plataformas.R
import com.example.lab8_plataformas.adapters.PlaceAdapter
import com.example.lab8_plataformas.dataStore.DataStore.dataStore
import com.example.lab8_plataformas.datasource.api.RetrofitInstance
import com.example.lab8_plataformas.datasource.local.Database
import com.example.lab8_plataformas.datasource.model.AllAssetsResponse
import com.example.lab8_plataformas.datasource.model.dataCharacters
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



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView = view.findViewById(R.id.recycler_recyclerActivity)
        toolbar = view.findViewById(R.id.toolbar_ToolbarActivity_characterDetail_characterList)

        database = Room.databaseBuilder(
            requireContext(),
            Database::class.java,
            "dataBaseCharacters"
        ).build()

        setListeners()
        setDataFromRoom()

    }

    private fun setDataFromRoom() {
        data.clear()
        CoroutineScope(Dispatchers.IO).launch {
            val users = database.dataCharacterDao().getUsers()
            data.addAll(users)
        }
        checkData()

    }

    private fun checkData() {

        if (data.size != 0){
            setupRecycler()
        }

        else{
            apiRequest()
        }
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
                    delete()
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
    }

    override fun onPlaceClicked(data: dataCharacters, position: Int) {
        requireView().findNavController().navigate(
            PlaceListFragmentDirections.actionPlaceListFragmentToPlaceDetailsFragment(
                characterID = data.id.toString()
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
                    }

                    setDataFromRoom()

                }
            }

            override fun onFailure(call: Call<AllAssetsResponse>, t: Throwable) {
                println("Error")
            }

        })

    }

    private fun delete() {
        CoroutineScope(Dispatchers.IO).launch {
            saveKeyValue(
                key = getString(R.string.keyInicioSesion)
            )
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
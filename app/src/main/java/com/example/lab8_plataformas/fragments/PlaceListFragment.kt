package com.example.lab8_plataformas.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab8_plataformas.R
import com.example.lab8_plataformas.adapters.PlaceAdapter
import com.example.lab8_plataformas.datasource.api.RetrofitInstance
import com.example.lab8_plataformas.datasource.model.AllAssetsResponse
import com.example.lab8_plataformas.datasource.model.Result
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceListFragment : Fragment(R.layout.fragment_place_list), PlaceAdapter.PlaceListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var placesList: MutableList<Result>
    private lateinit var buttonAZ: Button
    private lateinit var buttonZA: Button
    private lateinit var resultadoLlamadaAPI : MutableList<Result>
    private lateinit var toolbar: MaterialToolbar



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView = view.findViewById(R.id.recycler_recyclerActivity)
        toolbar = view.findViewById(R.id.toolbar_ToolbarActivity_characterDetail_characterList)

        apiRequest()
        setListeners()

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
                else -> true
            }
        }
    }

    private fun setupRecycler() {
        placesList = resultadoLlamadaAPI
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = PlaceAdapter(placesList, this)
    }

    override fun onPlaceClicked(data: Result, position: Int) {
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
                    resultadoLlamadaAPI = response.body()!!.results
                    setupRecycler()
                }
            }

            override fun onFailure(call: Call<AllAssetsResponse>, t: Throwable) {
                println("Error")
            }

        })

    }

}
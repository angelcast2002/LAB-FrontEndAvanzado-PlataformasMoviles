package com.example.lab8_plataformas.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab8_plataformas.R
import com.example.lab8_plataformas.adapters.PlaceAdapter
import com.example.lab8_plataformas.database.Character
import com.example.lab8_plataformas.database.RickAndMortyDB
import com.example.lab8_plataformas.datasource.api.RetrofitInstance
import com.example.lab8_plataformas.datasource.api.RickMortyAPI
import com.example.lab8_plataformas.datasource.model.AllAssetsResponse
import com.example.lab8_plataformas.datasource.model.AllAssetsResponseItem
import com.example.lab8_plataformas.datasource.model.PersonajesApi
import com.example.lab8_plataformas.datasource.model.PersonajesApi.obtenerPersonajesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import kotlin.random.Random

class PlaceListFragment : Fragment(R.layout.fragment_place_list), PlaceAdapter.PlaceListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var placesList: MutableList<Character>
    private lateinit var buttonAZ: Button
    private lateinit var buttonZA: Button



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_recyclerActivity)
        buttonAZ = view.findViewById(R.id.bt_sortAZ)
        buttonZA = view.findViewById(R.id.bt_sortZA)
        var listaIdString = ""

        setupRecycler()
        listaIdString = listaRandom()
        setListeners()
        apiRequest(listaIdString)
    }

    private fun listaRandom(): String {
        var ListaIdString = ""
        var listaNumeros = arrayListOf<Int>()
        for(i in 1..25){
            listaNumeros.add(Random(System.nanoTime()).nextInt(825 - 1 + 1) + 1)
        }
        var numeroInt: Int? = null
        var numeroString = ""
        for (i in 0..24){
            numeroInt = listaNumeros[i]
            numeroString = numeroInt.toString()
            if (i == 24){
                ListaIdString =  "${ListaIdString}${numeroString}"}
            else{
                ListaIdString =  "${ListaIdString}${numeroString},"
            }
        }
        return ListaIdString
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListeners() {
        buttonAZ.setOnClickListener{
            placesList.sortBy { place -> place.name }
            recyclerView.adapter!!.notifyDataSetChanged()
        }

        buttonZA.setOnClickListener{
            placesList.sortByDescending { place -> place.name }
            recyclerView.adapter!!.notifyDataSetChanged()
        }
    }

    private fun setupRecycler() {
        placesList = AllAssetsResponseItem.obtenerPersonajesApi()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = PlaceAdapter(placesList, this)
    }

    override fun onPlaceClicked(data: Character, position: Int) {
        requireView().findNavController().navigate(
            PlaceListFragmentDirections.actionPlaceListFragmentToPlaceDetailsFragment(
                data
            )
        )
    }

    private fun apiRequest(listaIdString:String) {
        RetrofitInstance.api.getCharacter(listaIdString).enqueue(object : Callback<AllAssetsResponse> {
            override fun onResponse(
                call: Call<AllAssetsResponse>,
                response: Response<AllAssetsResponse>
            ) {
                if (response.isSuccessful){
                    println(response.body())
                    var Respuesta: AllAssetsResponse? = response.body()
                    println(Respuesta)
                }
            }

            override fun onFailure(call: Call<AllAssetsResponse>, t: Throwable) {
                println("Error")
            }

        })
    }
}
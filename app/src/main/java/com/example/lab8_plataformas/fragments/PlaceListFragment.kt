package com.example.lab8_plataformas.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.util.CoilUtils.result
import com.example.lab8_plataformas.R
import com.example.lab8_plataformas.adapters.PlaceAdapter
import com.example.lab8_plataformas.database.Character
import com.example.lab8_plataformas.database.RickAndMortyDB
import com.example.lab8_plataformas.datasource.api.RetrofitInstance
import com.example.lab8_plataformas.datasource.model.AllAssetsResponse
import com.example.lab8_plataformas.datasource.model.Result
import com.example.lab8_plataformas.datasource.model.RickAndMortyDB.getCharacters
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

        setupRecycler()
        setListeners()
        apiRequest()
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
        placesList = Result
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

    private fun apiRequest() {
        RetrofitInstance.api.getCharacter().enqueue(object : Callback<AllAssetsResponse> {
            override fun onResponse(
                call: Call<AllAssetsResponse>,
                response: Response<AllAssetsResponse>
            ) {
                if (response.isSuccessful){
                    println(response.body())
                }
            }

            override fun onFailure(call: Call<AllAssetsResponse>, t: Throwable) {
                println("Error")
            }

        })
    }
}
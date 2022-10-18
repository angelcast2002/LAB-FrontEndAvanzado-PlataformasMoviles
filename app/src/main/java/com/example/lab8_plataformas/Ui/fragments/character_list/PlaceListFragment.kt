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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class PlaceListFragment : Fragment(R.layout.fragment_place_list), PlaceAdapter.PlaceListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var placesList: MutableList<dataCharacters>
    private  var data : MutableList<dataCharacters> = mutableListOf()
    private lateinit var toolbar: MaterialToolbar
    private lateinit var database: Database
    private lateinit var progressBar: ProgressBar

    private val viewModel: CharacterListViewModel by viewModels()




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
        setObservables()

    }

    private fun setObservables() {
        lifecycleScope.launchWhenStarted {
            viewModel.screenState.collectLatest { state ->
                handleState(state)
            }
        }
    }

    private fun handleState(state: CharacterListViewModel.ListUiState){
        when(state){
            CharacterListViewModel.ListUiState.Empty -> {
                

            }
            is CharacterListViewModel.ListUiState.Error -> {

            }
            CharacterListViewModel.ListUiState.Loading -> {

            }
            is CharacterListViewModel.ListUiState.Success -> {
                showCharacters(state.data, flase)
            }
        }
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
            getCharacters()
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
                    getCharacters()
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

    private fun getCharacters(){
        viewModel.getCharacterList()
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
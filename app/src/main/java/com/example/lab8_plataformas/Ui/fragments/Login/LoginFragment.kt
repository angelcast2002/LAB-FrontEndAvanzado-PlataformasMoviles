package com.example.lab8_plataformas.Ui.fragments.Login

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.lab8_plataformas.R
import com.example.lab8_plataformas.Data.dataStore.DataStore.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.login_fragment){
    private lateinit var buttonIniciarSesion: Button
    private lateinit var imageLogo: ImageView
    private lateinit var textCorreo: EditText
    private lateinit var correo: String
    private lateinit var textContrasenia: EditText
    private lateinit var contrasenia: String
    private lateinit var sesionIniciada : String



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonIniciarSesion = view.findViewById(R.id.button_iniciarSesion_loginfragment)
        imageLogo = view.findViewById(R.id.img_login_fragment)
        textCorreo = view.findViewById(R.id.textInput_correoText_loginFragment_editText)
        textContrasenia = view.findViewById(R.id.textInput_contrasenaText_loginFragment_editText)

        getValueDataStore()

    }

    private fun setListeners() {
        buttonIniciarSesion.setOnClickListener {

            correo = textCorreo.getText().toString()
            contrasenia = textContrasenia.getText().toString()

            if (correo == getString(R.string.textCorreo_fragment) && contrasenia == getString(R.string.textContrasenia_fragment)) {
                save()
            } else {
                Toast.makeText(getActivity(), getString(R.string.textToast), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun getValueDataStore(){
        CoroutineScope(Dispatchers.IO).launch {
            val value = getValueFromKey(
                key = getString(R.string.keyInicioSesion),
            )
            CoroutineScope(Dispatchers.Main).launch {
                sesionIniciada = value.toString()
                cargarVista()
            }
        }
    }

    private fun cargarVista() {
        if (sesionIniciada == getString(R.string.constant_iniciarSesion)){
            requireView().findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToPlaceListFragment()
            )
        }
        else(
                setListeners()
        )
    }

    private suspend fun getValueFromKey(key: String) : String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = context?.dataStore?.data?.first()
        return preferences?.get(dataStoreKey)
    }

    private fun save() {
        CoroutineScope(Dispatchers.IO).launch {
            saveKeyValue(
                key = getString(R.string.keyInicioSesion),
                value = getString(R.string.constant_iniciarSesion),
            )
        }

        requireView().findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToPlaceListFragment()
        )
    }

    private suspend fun saveKeyValue(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context?.dataStore?.edit { settings ->
            settings[dataStoreKey] = value
        }
    }
}
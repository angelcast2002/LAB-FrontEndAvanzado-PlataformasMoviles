package com.example.lab8_plataformas.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.lab8_plataformas.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.login_fragment){
    private lateinit var buttonIniciarSesion: Button
    private lateinit var imageLogo: ImageView
    private lateinit var textCorreo: EditText
    private lateinit var correo: String
    private lateinit var textContrasenia: EditText
    private lateinit var contrasenia: String
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonIniciarSesion = view.findViewById(R.id.button_iniciarSesion_loginfragment)
        imageLogo = view.findViewById(R.id.img_login_fragment)
        textCorreo = view.findViewById(R.id.textInput_correoText_loginFragment_editText)
        textContrasenia = view.findViewById(R.id.textInput_contrasenaText_loginFragment_editText)

        setListeners()

    }

    private fun setListeners() {
        buttonIniciarSesion.setOnClickListener {

            correo = textCorreo.getText().toString()
            contrasenia = textContrasenia.getText().toString()

            if (correo == getString(R.string.textCorreo_fragment) && contrasenia == getString(R.string.textContrasenia_fragment)) {
                requireView().findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToPlaceListFragment()
                )
            } else {
                Toast.makeText(getActivity(), getString(R.string.textToast), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}
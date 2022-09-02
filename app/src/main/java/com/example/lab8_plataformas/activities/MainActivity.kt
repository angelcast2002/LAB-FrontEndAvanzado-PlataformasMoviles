package com.example.lab8_plataformas.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.lab8_plataformas.R

class MainActivity : AppCompatActivity() {
    private lateinit var topAppBar: Toolbar
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configuramos nuestra toolbar
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.main_fragment_view
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Configuramos el Login y el Home como "top level" destinations
        val appbarConfig = AppBarConfiguration(setOf(R.id.placeListFragment, R.id.placeDetailsFragment))
        topAppBar = findViewById(R.id.toolbar_ToolbarActivity)
        topAppBar.setupWithNavController(navController, appbarConfig)

        setListeners()
        setNavigation()
    }

    private fun setNavigation() {
        topAppBar.visibility = View.VISIBLE
    }

    private fun setListeners() {
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_AZ -> {
                    Toast.makeText(this,"Funciona", Toast.LENGTH_LONG).show()
                    true
                }

                R.id.menu_item_ZA -> {
                    Toast.makeText(this,"Funciona", Toast.LENGTH_LONG).show()
                    true
                }

                else -> false
            }
        }
    }
}
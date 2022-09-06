package com.example.lab8_plataformas.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
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

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_view) as NavHostFragment
        navController = navHostFragment.navController

        val appbarConfig = AppBarConfiguration(navController.graph)
        topAppBar = findViewById(R.id.toolbar_ToolbarActivity)
        topAppBar.setupWithNavController(navController, appbarConfig)

        setListeners()
        setNavigation()

    }

    private fun setNavigation() {

        navController.addOnDestinationChangedListener{_, destinacion,_ ->
            when(destinacion.id){
                R.id.placeListFragment -> {
                    topAppBar.visibility = View.VISIBLE
                    topAppBar.menu.findItem(R.id.menu_item_AZ).isVisible = true
                    topAppBar.menu.findItem(R.id.menu_item_ZA).isVisible = true
                }

                R.id.placeDetailsFragment -> {
                    topAppBar.menu.findItem(R.id.menu_item_AZ).isVisible = false
                    topAppBar.menu.findItem(R.id.menu_item_ZA).isVisible = false
                }
            }
        }

    }

    private fun setListeners() {
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_AZ -> {
                    Toast.makeText(this,getString(R.string.textMensaje_topAppBar), Toast.LENGTH_LONG).show()
                    //placesList = placesList.sortedBy { it.name } as MutableList<Character>
                    true
                }

                R.id.menu_item_ZA -> {
                    Toast.makeText(this,getString(R.string.textMensaje_topAppBar), Toast.LENGTH_LONG).show()
                    true
                }


                else -> false
            }
        }
    }
}
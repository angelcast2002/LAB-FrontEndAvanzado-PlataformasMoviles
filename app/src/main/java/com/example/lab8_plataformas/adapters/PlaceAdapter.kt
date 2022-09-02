package com.example.lab8_plataformas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.lab8_plataformas.R
import com.example.lab8_plataformas.database.Character
import com.example.lab8_plataformas.database.RickAndMortyDB

class PlaceAdapter(
    private val dataSet: MutableList<Character>,
    private val placeListener: PlaceListener
):
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    interface PlaceListener {
        fun onPlaceClicked(data: Character, position: Int)
    }

    class ViewHolder(private val view: View,
                     private val listener: PlaceListener) : RecyclerView.ViewHolder(view) {
        private val imageType: ImageView = view.findViewById(R.id.imageView_recycleViewPlace)
        private val textName: TextView = view.findViewById(R.id.textView_recycleViewPlace_name)
        private val textRaceStatus: TextView = view.findViewById(R.id.textView_recycleViewPlace_raceStatus)
        private val layout: ConstraintLayout = view.findViewById(R.id.layout_itemPlace)
        private lateinit var place: Character

        fun setData(place: Character) {
            this.place = place
            textName.text = place.name
            //textRaceStatus.text = place.race + " - " + place.status
            (place.species + " - " + place.status).also { textRaceStatus.text = it }

            imageType.load(place.image) {
                transformations(CircleCropTransformation())
                //error(R.drawable.ic_error)
                //placeholder(R.drawable.ic_downloading)
            }
            setListeners()
        }

        private fun setListeners() {
            layout.setOnClickListener {
                listener.onPlaceClicked(place, this.adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_object, parent, false)

        return ViewHolder(view, placeListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

}
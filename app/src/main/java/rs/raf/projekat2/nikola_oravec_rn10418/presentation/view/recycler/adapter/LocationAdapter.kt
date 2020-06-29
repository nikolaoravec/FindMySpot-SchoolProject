package rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import rs.raf.projekat2.nikola_oravec_rn10418.R
import rs.raf.projekat2.nikola_oravec_rn10418.data.models.Location
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.recycler.diff.LocationDiffCallback
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.recycler.viewholder.LocationViewHolder


class LocationAdapter (diff : LocationDiffCallback,private val onClicked: (Location,String) -> Unit) : ListAdapter<Location, LocationViewHolder>(diff){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.location_item, parent, false)
        return LocationViewHolder(view) { adapterPosition: Int, repsonse : String ->
            val location = getItem(adapterPosition)
            onClicked.invoke(location,repsonse)
        }
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
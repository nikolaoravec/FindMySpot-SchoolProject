package rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import rs.raf.projekat2.nikola_oravec_rn10418.data.models.Location


class LocationDiffCallback : DiffUtil.ItemCallback<Location>() {

    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem.x==newItem.x && oldItem.y==newItem.y && oldItem.title==newItem.title && oldItem.content==newItem.content
    }

}
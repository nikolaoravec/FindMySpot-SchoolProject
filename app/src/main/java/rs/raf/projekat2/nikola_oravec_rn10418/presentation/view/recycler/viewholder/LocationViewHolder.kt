package rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.recycler.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.location_item.*
import rs.raf.projekat2.nikola_oravec_rn10418.data.models.Location


class LocationViewHolder(override val containerView: View, onItemClicked: (Int,String) -> Unit) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    init{
        iw_pin.setOnClickListener{
            onItemClicked.invoke(adapterPosition,"edit")
        }
        iw_delete.setOnClickListener{
            onItemClicked.invoke(adapterPosition,"delete")
        }


    }

    fun bind(l: Location) {
        location_title.text = l.title
        location_content.text = l.content
        location_date.text = l.date.toString()
    }

}
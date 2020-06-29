package rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.contract

import androidx.lifecycle.LiveData
import rs.raf.projekat2.nikola_oravec_rn10418.data.models.Location
import rs.raf.projekat2.nikola_oravec_rn10418.data.models.LocationFilter
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.states.ChangeLocationState
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.states.LocationsState


interface Contract {

    interface ViewModel {

        val locationsState: LiveData<LocationsState>
        val changeDone: LiveData<ChangeLocationState>

        fun getAll()
        fun addLocation(l : Location)
        fun getAllByFilter(locationFilter: LocationFilter)
        fun updateTitleAndContentById(id:Long,title:String,content:String)
        fun delete(id:Long)
    }
}
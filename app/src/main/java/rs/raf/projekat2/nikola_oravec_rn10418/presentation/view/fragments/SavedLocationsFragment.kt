package rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.projekat2.nikola_oravec_rn10418.R
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.contract.Contract
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.states.LocationsState
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.viewmodel.ViewModel
import timber.log.Timber

class SavedLocationsFragment : Fragment(R.layout.fragment_locations), OnMapReadyCallback{

    private val viewModel: Contract.ViewModel by sharedViewModel<ViewModel>()
    private var locations : MutableList<rs.raf.projekat2.nikola_oravec_rn10418.data.models.Location> = mutableListOf()

    private var mMap: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map2) as SupportMapFragment?

        mapFragment?.getMapAsync(this)

        initObservers()
    }

    override fun onResume() {
        super.onResume()
            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map2) as SupportMapFragment?

            mapFragment?.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap?.uiSettings?.isZoomControlsEnabled = true
        mMap?.uiSettings?.isZoomGesturesEnabled = true
        mMap?.uiSettings?.isCompassEnabled = true

        mMap?.clear()
        locations.forEach {
            val location = LatLng(it.x,it.y)
            mMap!!.addMarker(MarkerOptions().position(location).title(it.title))
        }
        Timber.e("usao sam ")
    }
    private fun initObservers(){
        viewModel.locationsState.observe(viewLifecycleOwner, Observer {
            renderState(it)
        })

        viewModel.getAll()
    }

    private fun renderState(state : LocationsState) {
        when (state) {
            is LocationsState.Success -> {
                locations.clear()
                state.locations.map {

                    val location = rs.raf.projekat2.nikola_oravec_rn10418.data.models.Location(
                            it.id,
                            it.x,
                            it.y,
                            it.title,
                            it.content,
                            it.date
                        )

                    locations.add(location)
                }
            }
            is LocationsState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is LocationsState.Update -> {
                viewModel.getAll()
            }
            is LocationsState.Add -> {
                viewModel.getAll()
            }
        }
    }
}

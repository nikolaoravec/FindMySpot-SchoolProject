package rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import rs.raf.projekat2.nikola_oravec_rn10418.R
import rs.raf.projekat2.nikola_oravec_rn10418.data.models.Location
import rs.raf.projekat2.nikola_oravec_rn10418.data.models.LocationFilter
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.activities.ChangeLocationActivity
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.contract.Contract
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.recycler.adapter.LocationAdapter
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.recycler.diff.LocationDiffCallback
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.states.LocationsState
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.viewmodel.ViewModel
import timber.log.Timber

class ListFragment  : Fragment(R.layout.fragment_list) {

    private val viewModel: Contract.ViewModel by sharedViewModel<ViewModel>()
    private var order = true

    companion object {
        const val REQUEST_CODE = 1
        const val RECEVED_KEY = "requestCode"
    }

    private lateinit var adapter: LocationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initUi()
        initObservers()
    }

    private fun initUi() {
        initRecycler()
        initListeners()
    }

    private fun initRecycler() {
        rv_locations.layoutManager = LinearLayoutManager(context)
        adapter = LocationAdapter(LocationDiffCallback()) { location: Location, response : String ->
            if (response=="edit") {
                val intent = Intent(this.activity, ChangeLocationActivity::class.java)
                intent.putExtra(ChangeLocationActivity.CHANGE_KEY, location)
                startActivityForResult(intent, REQUEST_CODE)
            }else{
                viewModel.delete(location.id)
            }
        }
        rv_locations.adapter = adapter
    }

    private fun initListeners() {

        search_titleCo.addTextChangedListener {
                val titleContent : String = search_titleCo.text.toString()
                val locationFilter = LocationFilter(titleContent,false)
                viewModel.getAllByFilter(locationFilter)
         }

        btn_filter.setOnClickListener{
            val titleContent : String = search_titleCo.text.toString()
            val locationFilter = LocationFilter(titleContent,!order)
            viewModel.getAllByFilter(locationFilter)
            order = !order
        }

    }

    private fun initObservers() {
        viewModel.locationsState.observe(viewLifecycleOwner, Observer {
             renderState(it)
        })

        val titleContent : String = search_titleCo.text.toString()
        val locationFilter = LocationFilter(titleContent,!order)
        viewModel.getAllByFilter(locationFilter)
    }

    private fun renderState(state : LocationsState) {
        when (state) {
            is LocationsState.Success -> {
                adapter.submitList(state.locations)
            }
            is LocationsState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is LocationsState.Update -> {
                Timber.e("Update success")
            }
            is LocationsState.Add -> {
                Timber.e("Add success")
            }
            is LocationsState.Delete -> {
                Timber.e("Delete success")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE -> {
                    if (resultCode == Activity.RESULT_OK) {
                        return
                    } else{
                        return
                    }
                }
            }
        }
    }


}
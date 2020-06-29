package rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_change_location.*
import kotlinx.android.synthetic.main.activity_maps.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import rs.raf.projekat2.nikola_oravec_rn10418.R
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.contract.Contract
import java.io.IOException
import java.util.*

class MapsActivity : FragmentActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener,	GoogleMap.OnMapClickListener {

    private val viewModel: Contract.ViewModel by viewModel<rs.raf.projekat2.nikola_oravec_rn10418.presentation.viewmodel.ViewModel>()

    var mGoogleApiClient: GoogleApiClient? = null
    var mLastLocation: Location? = null
    var mCurrLocationMarker: Marker? = null
    var mLocationRequest: LocationRequest? = null
    var x: Double? = null
    var y: Double? = null
    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initObservers()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap?.uiSettings?.isZoomControlsEnabled = true
        mMap?.uiSettings?.isZoomGesturesEnabled = true
        mMap?.uiSettings?.isCompassEnabled = true
        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                === PackageManager.PERMISSION_GRANTED
            ) {
                buildGoogleApiClient()
                mMap?.isMyLocationEnabled = true
            }
        } else {
            buildGoogleApiClient()
            mMap?.isMyLocationEnabled = true
        }
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient?.connect()
    }

    override fun onConnected(bundle: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest?.setInterval(5000)
        mLocationRequest?.setFastestInterval(1000)
        mLocationRequest?.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            === PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest, this
            )
        }
    }

    override fun onConnectionSuspended(i: Int) {}

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker?.remove()
        }
        //Showing Current Location Marker on Map
        val latLng = LatLng(location.latitude, location.longitude)
        x = location.latitude
        y = location.longitude
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val provider = locationManager.getBestProvider(Criteria(), true)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            !== PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val locations = locationManager.getLastKnownLocation(provider)
        val providerList = locationManager.allProviders
        if (null != locations && null != providerList && providerList.size > 0) {
            val longitude = locations.longitude
            val latitude = locations.latitude
            val geocoder = Geocoder(
                getApplicationContext(),
                Locale.getDefault()
            )
            try {
                val listAddresses = geocoder.getFromLocation(
                    latitude,
                    longitude, 1
                )
                if (null != listAddresses && listAddresses.size > 0) {
                    val state = listAddresses[0].adminArea
                    val country = listAddresses[0].countryName
                    val subLocality = listAddresses[0].subLocality
                    markerOptions.title(
                        "" + latLng + "," + subLocality + "," + state
                                + "," + country
                    )
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        mCurrLocationMarker = mMap?.addMarker(markerOptions)
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap?.animateCamera(CameraUpdateFactory.zoomTo(11f))
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                this
            )
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            !== PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        === PackageManager.PERMISSION_GRANTED
                    ) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient()
                        }
                        mMap?.isMyLocationEnabled = true
                    }
                } else {
                    Toast.makeText(
                        this,
                        "You have to give a permission if you want to access the map.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }

    override fun onMapClick(p0: LatLng?) {
        TODO("Not yet implemented")
    }

    private fun initObservers() {

        btn_odustani.setOnClickListener {
            et_title.text.clear()
            et_content.text.clear()
        }

        btn_dodaj.setOnClickListener {
            if (check()) {
                val location = x?.let { it1 ->

                y?.let { it2 ->
                    rs.raf.projekat2.nikola_oravec_rn10418.data.models.Location(
                        0,
                        it1, it2, et_title.text.toString(), et_content.text.toString(), Date()
                    )
                }
            }
            if (location != null) {
                viewModel.addLocation(location)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}

    private fun check():Boolean{
        if (et_content.text.isEmpty() || et_title.text.isEmpty()) {
            Toast.makeText(this, "Fields must not be empty!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}

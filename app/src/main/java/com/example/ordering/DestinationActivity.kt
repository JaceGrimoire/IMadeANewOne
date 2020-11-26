package com.example.ordering

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isInvisible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.IOException
import java.util.*

class DestinationActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val EXTRA_USER_ORIGIN_LAT = "origin_lat"
        const val EXTRA_USER_ORIGIN_LONG = "origin_long"
        const val EXTRA_USER_DEST_LAT = "dest_lat"
        const val EXTRA_USER_DEST_LONG = "dest_long"
    }

    private lateinit var googleMap: GoogleMap

    private lateinit var toolbar: Toolbar
    private lateinit var fabMyLocation: FloatingActionButton
    private lateinit var txtDestination: TextView
    private lateinit var pbDestination: ProgressBar
    private lateinit var btnNext: Button

    private val defaultLatLng = LatLng(18.1960, 120.5927)
    private var userLatLng: LatLng? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager

    private var mapUpdated = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination)

        Log.i(localClassName, "onCreate has been called")

        toolbar = findViewById(R.id.toolbar1)
        toolbar.title = "Your Destination"
        setSupportActionBar(toolbar);

        fabMyLocation = findViewById(R.id.fabLocation1)
        fabMyLocation.setOnClickListener { getLocation() }

        pbDestination = findViewById(R.id.pbDestination)

        txtDestination = findViewById(R.id.txtDestination1)

        btnNext = findViewById(R.id.btnNext1)
        btnNext.setOnClickListener { next() }

        mapInitialization()
    }

    private fun mapInitialization() {
        Log.d(localClassName, "Initializing the map fragment")
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.destination_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Update the address text in a fixed rate
        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        val timer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                if (mapUpdated) {
                    val location = googleMap.cameraPosition.target
                    try {
                        txtDestination.text =
                                geocoder.getFromLocation(
                                        location.latitude,
                                        location.longitude,
                                        1
                                )[0]
                                        .getAddressLine(0)

                        // Update user latlng
                        userLatLng = location
                    } catch (e: IOException) {
                        Log.w(localClassName, "Failed to load the geo")
                    }
                    mapUpdated = false
                    pbDestination.isInvisible = false
                }
                start()
            }
        }
        timer.start()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLatLng))
        this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
        this.googleMap.setOnCameraMoveListener {
            if (!mapUpdated) {
                mapUpdated = true
                pbDestination.isInvisible = false
            }
        }
    }

    private fun getLocation() {
        // Setup the proper permission first to locate the device
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
                )
                .withListener(object : MultiplePermissionsListener, PermissionListener {
                    @SuppressLint("MissingPermission")
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (report.areAllPermissionsGranted()) {
                                // Permission granted
                                toast("Identifying your current location...")
                                // Get the current location
//                            fusedLocationClient.lastLocation.addOnSuccessListener {
//                                Log.i(localClassName, "Successfully listened to location")
//                                if (it == null) {
//                                    Log.e(localClassName, "The retrieved location is null")
//                                } else {
//                                    Log.i(localClassName, "The location has been retrieved")
//                                }
//                            }
                                fusedLocationClient.lastLocation.addOnCompleteListener {
                                    val location = it.result
                                    if (location == null) {
                                        Log.e(localClassName, "The location is null")
                                    } else {
                                        Log.i(localClassName, "The location has been retrieved")
                                        Log.d(
                                                localClassName,
                                                "LatLng: ${location.latitude}:${location.longitude}"
                                        )
                                        googleMap.moveCamera(
                                                CameraUpdateFactory.newLatLng(
                                                        LatLng(
                                                                location.latitude,
                                                                location.longitude
                                                        )
                                                )
                                        )
                                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
                                        mapUpdated = true
                                        pbDestination.isInvisible = false
                                    }
                                }
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                            permissions: MutableList<PermissionRequest>?,
                            token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }

                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        toast("Permission has been granted")
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        toast("We cannot access your location.")
                    }

                    override fun onPermissionRationaleShouldBeShown(
                            p0: PermissionRequest?,
                            p1: PermissionToken?
                    ) {
                        toast("Your location is needed for the app to locate your current address.")
                    }

                })
                .withErrorListener {
                    toast(it.name)
                }
                .check();
    }

    private fun next() {
        Log.i(localClassName, "Intent to ConfirmationActivity")
        if (userLatLng != null) {
            val intent = Intent(applicationContext, ConfirmationActivity::class.java).apply {
                // Origin address
                putExtra(
                        EXTRA_USER_ORIGIN_LONG,
                        intent.getDoubleExtra(OriginActivity.EXTRA_USER_LONG, 0.0)
                )
                putExtra(
                        EXTRA_USER_ORIGIN_LAT,
                        intent.getDoubleExtra(OriginActivity.EXTRA_USER_LAT, 0.0)
                )
                // Destination address
                putExtra(
                        EXTRA_USER_DEST_LAT,
                        userLatLng!!.latitude
                )
                putExtra(
                        EXTRA_USER_DEST_LONG,
                        userLatLng!!.longitude
                )
            }
            startActivity(intent)
        } else {
            toast("Please select a destination address first.")
        }
    }

    fun toast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

}
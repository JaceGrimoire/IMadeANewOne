package com.example.ordering

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
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


class OriginActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val EXTRA_USER_LAT = "origin_lat"
        const val EXTRA_USER_LONG = "origin_long"
    }

    private lateinit var googleMap: GoogleMap

    private lateinit var toolbar: Toolbar
    private lateinit var fabLocation: FloatingActionButton
    private lateinit var pbOrigin: ProgressBar
    private lateinit var txtOriginAddress: TextView
    private lateinit var btnNext: Button

    private val defaultLatLng = LatLng(18.1960, 120.5927)
    private var userLatLng: LatLng? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var mapUpdated = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_origin)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        toolbar = findViewById(R.id.toolbar_origin)
        toolbar.title = "Your Origin"
        setSupportActionBar(toolbar);

        fabLocation = findViewById(R.id.fabLocation0)
        fabLocation.setOnClickListener { getLocation() }

        txtOriginAddress = findViewById(R.id.txtOrigin0)

        pbOrigin = findViewById(R.id.pbOrigin0)

        btnNext = findViewById(R.id.btnNext)
        btnNext.setOnClickListener { next() }

        mapInitialization()
    }

    private fun mapInitialization() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.origin_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Update the address text in a fixed rate
        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        val timer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                if (mapUpdated) {
                    val location = googleMap.cameraPosition.target
                    try {
                        txtOriginAddress.text =
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
                    pbOrigin.isInvisible = true
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
                pbOrigin.isInvisible = false
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
                                        pbOrigin.isInvisible = false
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

    fun toast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun next() {
        if (userLatLng != null) {
            Log.d(localClassName, "Going to destination Activity")
            val destination =
                    Intent(applicationContext, DestinationActivity::class.java).apply {
                        putExtra(EXTRA_USER_LAT, userLatLng?.latitude)
                        putExtra(EXTRA_USER_LONG, userLatLng?.longitude)
                    }
            startActivity(destination)
        } else {
            toast("Please set an origin address first.")
        }
    }
}
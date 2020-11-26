package com.example.ordering


import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import java.util.*

class ConfirmationActivity : AppCompatActivity() {

    private lateinit var tbConfirmation: Toolbar

    private lateinit var txtOriginAddress: TextView
    private lateinit var txtDestinationAddress: TextView

    private lateinit var btnConfirm: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        Log.i(localClassName, "onCreate has been called")

        tbConfirmation = findViewById(R.id.tbConfirmation)
        tbConfirmation.title = "Verify your address"
        setSupportActionBar(tbConfirmation)

        txtOriginAddress = findViewById(R.id.txtOrigin0)
        txtDestinationAddress = findViewById(R.id.txtDestinationAddress)
        btnConfirm = findViewById(R.id.btnConfirm)
        btnConfirm.setOnClickListener { confirm() }

        // Initialization of data from intent extra
        val originLat = intent.getDoubleExtra(DestinationActivity.EXTRA_USER_ORIGIN_LAT, 0.0)
        val originLong = intent.getDoubleExtra(DestinationActivity.EXTRA_USER_ORIGIN_LONG, 0.0)
        val destLat = intent.getDoubleExtra(DestinationActivity.EXTRA_USER_DEST_LAT, 0.0)
        val destLong = intent.getDoubleExtra(DestinationActivity.EXTRA_USER_DEST_LONG, 0.0)

        Log.d(
                localClassName,
                "Intent = [originLat=$originLat, originLong=$originLong, destLat=@$destLat, destLong=$destLong]"
        )

        // Display the addresses
        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        txtOriginAddress.text =
                geocoder.getFromLocation(originLat, originLong, 1)[0].getAddressLine(0)
        txtDestinationAddress.text =
                geocoder.getFromLocation(destLat, destLong, 1)[0].getAddressLine(0)
    }

    private fun confirm() {
        Log.i(localClassName, "User confirmed the origin and destination addresses")
        val intent = Intent(this, WaitingRideActivity::class.java).apply {
            putExtra(
                    DestinationActivity.EXTRA_USER_ORIGIN_LAT,
                    intent.getDoubleExtra(DestinationActivity.EXTRA_USER_ORIGIN_LAT, 0.0)
            )
            putExtra(
                    DestinationActivity.EXTRA_USER_ORIGIN_LONG,
                    intent.getDoubleExtra(DestinationActivity.EXTRA_USER_ORIGIN_LONG, 0.0)
            )
            putExtra(
                    DestinationActivity.EXTRA_USER_DEST_LONG,
                    intent.getDoubleExtra(DestinationActivity.EXTRA_USER_DEST_LAT, 0.0)
            )
            putExtra(
                    DestinationActivity.EXTRA_USER_DEST_LONG,
                    intent.getDoubleExtra(DestinationActivity.EXTRA_USER_DEST_LONG, 0.0)
            )
        }
        startActivity(intent)
    }
}
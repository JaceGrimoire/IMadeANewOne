package com.example.ordering

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*

class WaitingRideActivity : AppCompatActivity() {

    lateinit var database: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var mAuth: FirebaseAuth
    lateinit var txtMessage : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_ride)

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        val uid = mAuth.currentUser?.uid

        val originLat = intent.getDoubleExtra(DestinationActivity.EXTRA_USER_ORIGIN_LAT, 0.0)
        val originLong = intent.getDoubleExtra(DestinationActivity.EXTRA_USER_ORIGIN_LONG, 0.0)
        val destLat = intent.getDoubleExtra(DestinationActivity.EXTRA_USER_DEST_LAT, 0.0)
        val destLong = intent.getDoubleExtra(DestinationActivity.EXTRA_USER_DEST_LONG, 0.0)
        txtMessage = findViewById(R.id.textView4);

        val location = Location(originLat, originLong, destLat,destLong,uid,"0", "waiting")
        Log.d("Test", "$originLat $originLong $destLat $destLong");
        reference = database.getReference("rider_requests");
        val key = reference.push().key;
        if (key != null) {
            reference.child(key).setValue(location)
        }
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.getValue<Location>()
                if (post != null) {
                    if(post.getState() == "responded") {
                        txtMessage.setText("A rider has responded to you! Standby for the arrival")
                    }
                    else if(post.getState() == "arrived")
                        txtMessage.setText("The rider has arrived to pick you up! Have a safe trip!")
                    else if(post.getState() == "completed")
                        txtMessage.setText("You have arrived at your destination! Thank you for riding with us!")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TEST", error.message)
            }
        }
        if (key != null) {
            reference.child(key).addValueEventListener(postListener)
        }
    }
}
package com.UTVT.busconductor

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.MapView
import com.google.firebase.database.*

class ReceptorActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private var marker: Marker? = null

    private val database = FirebaseDatabase.getInstance()
    private val locationRef = database.getReference("locations") // Referencia a "locations"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receptor)

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        listenForLocationUpdates() // Llamamos a la función para escuchar actualizaciones
    }

    private fun listenForLocationUpdates() {
        // Usamos addChildEventListener para escuchar cambios más rápidos
        locationRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                updateLocation(snapshot)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                updateLocation(snapshot)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Opcional: Manejar la eliminación de un marcador si es necesario
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Opcional: Manejar el movimiento de un marcador si es necesario
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores de Firebase
                Toast.makeText(this@ReceptorActivity, "Error al obtener la ubicación", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateLocation(snapshot: DataSnapshot) {
        val lat = snapshot.child("latitude").getValue(Double::class.java)
        val lng = snapshot.child("longitude").getValue(Double::class.java)

        if (lat != null && lng != null) {
            val position = LatLng(lat, lng)
            if (marker == null) {
                marker = googleMap.addMarker(MarkerOptions().position(position).title("Emisor"))
            } else {
                marker?.position = position
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f)) // Centra el mapa en la nueva ubicación
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}

package com.UTVT.busconductor

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EmisorActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var database: FirebaseDatabase
    private lateinit var locationRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emisor)

        // Inicializar Firebase
        FirebaseApp.initializeApp(this)
        database = FirebaseDatabase.getInstance()
        locationRef = database.getReference("locations")

        // Inicializar cliente de localización
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Configurar la solicitud de ubicación
        locationRequest = LocationRequest.create().apply {
            interval = 5000 // Actualización cada 5 segundos
            fastestInterval = 3000 // Intervalo más rápido de actualización
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // Inicializar LocationCallback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let { location ->
                    val latitude = location.latitude
                    val longitude = location.longitude

                    // Actualizar ubicación en Firebase
                    locationRef.setValue(mapOf("latitude" to latitude, "longitude" to longitude))
                    Toast.makeText(this@EmisorActivity, "Ubicación enviada", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Verificar permisos de localización
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_LOCATION_PERMISSION
            )
            return
        }

        // Comenzar a recibir actualizaciones de ubicación
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback) // Detener actualizaciones de ubicación
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }
}

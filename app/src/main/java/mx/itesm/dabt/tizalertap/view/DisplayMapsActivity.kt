package mx.itesm.dabt.tizalertap.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import mx.itesm.dabt.tizalertap.EXTRA_USER_MAP
import mx.itesm.dabt.tizalertap.R
import mx.itesm.dabt.tizalertap.databinding.ActivityDisplayMapsBinding
import mx.itesm.dabt.tizalertap.model.DataNotificacion

/* This class is responsible for displaying a map with a marker on it */
class DisplayMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDisplayMapsBinding
    lateinit var userMap: DataNotificacion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDisplayMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userMap = intent.getSerializableExtra(EXTRA_USER_MAP) as DataNotificacion

        supportActionBar?.title = userMap.titulo


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        println("Preuba de lo desplegable ${userMap.titulo}")
        val atizapan = LatLng(19.589693, -99.229509)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(atizapan, 13F))
        mMap = googleMap

        /*val sydney = LatLng(-33.852, 151.211)
        googleMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
        val latLng = LatLng(userMap.latitud.toDouble(), userMap.longitud.toDouble())
        println(userMap)
        println(latLng)
        mMap.addMarker(MarkerOptions().position(latLng).title(userMap.titulo).snippet(userMap.descripcion))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13F))

        // Add a marker in Sydney and move the camera

    }
}
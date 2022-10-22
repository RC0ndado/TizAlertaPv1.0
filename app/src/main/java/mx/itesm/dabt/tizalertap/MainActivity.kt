package mx.itesm.dabt.tizalertap

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import mx.itesm.dabt.tizalertap.databinding.ActivityMainBinding
import mx.itesm.dabt.tizalertap.model.DataNotificacion
import mx.itesm.dabt.tizalertap.view.AdaptadorNotificaciones
import mx.itesm.dabt.tizalertap.viewmodel.ListaNotiVM
import java.util.jar.Manifest
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import mx.itesm.dabt.tizalertap.view.ActivityMostrarResumenClima
import mx.itesm.dabt.tizalertap.R
import mx.itesm.dabt.tizalertap.databinding.ActivityMostrarResumenClimaBinding
import mx.itesm.dabt.tizalertap.model.UserMapa
import kotlin.math.roundToInt
import mx.itesm.dabt.tizalertap.view.DisplayMapsActivity
import mx.itesm.dabt.tizalertap.view.Contactos
import mx.itesm.dabt.tizalertap.view.Creditos


const val EXTRA_USER_MAP = "EXTRA_USER_MAP"

/* The main activity of the app, it is the first activity that is shown when the app is opened. */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel : ListaNotiVM by viewModels()
    private lateinit var adaptador : AdaptadorNotificaciones
    private lateinit var btnEmergencia: ImageButton
    private lateinit var userMap: UserMapa

    /**
     * The onCreate function of the MainActivity. It is called when the app is created.
     * 
     * @param savedInstanceState If the activity is being re-initialized after previously being shut
     * down then this Bundle contains the data it most recently supplied in
     * onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Firebase.messaging.subscribeToTopic("alertasAtizapan")
            .addOnCompleteListener{task ->
                var msg = "Subcribed"
                if (!task.isSuccessful){
                    msg = "Subscribe failed"
                }
                println(msg)
            }
        btnEmergencia = findViewById(R.id.btnEmerg)
        checkPermissions()
        configurarRV()
        configurarObservable()
        supportActionBar?.title = "TizAlerta"

        btnEmergencia.setOnClickListener {
            mostrarAdvertencia()
        }

        // Boton Clima
        mostrarResumenClima()

        // API Clima
        mostrarClima()
    }

    private fun mostrarClima() {
        val queue = Volley.newRequestQueue(this)
        val url =
            "https://api.openweathermap.org/data/2.5/weather?q=Atizapán,MX&appid=c6747b86f2e57d515b3cdcdff6c68a35&units=metric&lang=es"

        val weatherRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Parte centrica
                val main = response.getJSONObject("main")
                val temp = main.getString("temp")

                // Descripcion de clima
                val clima = response.getJSONArray("weather")
                val primerIndice = clima.getJSONObject(0)
                val climaMain = primerIndice.getString("main")

                val dt = response.getString("dt")
                val sys = response.getJSONObject("sys")
                val sunset = sys.getString("sunset")

                val key: String

                if (dt < sunset) {
                    key = climaMain + "Dia"
                    println(key)
                } else {
                    key = climaMain + "Noche"
                    println(key)
                }

                when (key) {
                    "ClearDia" -> {
                        binding.iconoClima.setImageResource(R.drawable.clear_sky_day)
                        binding.fondoClima.setBackgroundResource(R.drawable.clear_sky_background)
                        binding.tvDescripcionClima.setText("Despejado")
                    }
                    "ClearNoche" -> {
                        binding.iconoClima.setImageResource(R.drawable.clear_sky_moon)
                        binding.fondoClima.setBackgroundResource(R.drawable.clear_sky_night_background)
                        binding.tvDescripcionClima.setText("Despejado")
                    }
                    "CloudsDia" -> {
                        binding.iconoClima.setImageResource(R.drawable.few_clouds_day)
                        binding.fondoClima.setBackgroundResource(R.drawable.few_clouds_background)
                        binding.tvDescripcionClima.setText("Nublado")
                    }
                    "CloudsNoche" -> {
                        binding.iconoClima.setImageResource(R.drawable.few_clouds_moon)
                        binding.fondoClima.setBackgroundResource(R.drawable.few_clouds_night_background)
                        binding.tvDescripcionClima.setText("Nublado")
                    }
                }

                when(climaMain){
                    "Drizzle" -> {
                        binding.iconoClima.setImageResource(R.drawable.shower_rain)
                        binding.fondoClima.setBackgroundResource(R.drawable.shower_rain_background)
                        binding.tvDescripcionClima.setText("Llovizna")
                    }
                    "Rain" -> {
                        binding.iconoClima.setImageResource(R.drawable.rain)
                        binding.fondoClima.setBackgroundResource(R.drawable.rain_background)
                        binding.tvDescripcionClima.setText("Lluvia")
                    }
                    "Thunderstorm" -> {
                        binding.iconoClima.setImageResource(R.drawable.thunderstorm)
                        binding.fondoClima.setBackgroundResource(R.drawable.thunderstorm_background)
                        binding.tvDescripcionClima.setText("Tormeta")
                    }
                    "Snow" -> {
                        binding.iconoClima.setImageResource(R.drawable.snow)
                        binding.fondoClima.setBackgroundResource(R.drawable.snow_background)
                        binding.tvDescripcionClima.setText("Nevado")
                    }
                    "Atmosphere" -> {
                        binding.iconoClima.setImageResource(R.drawable.mist)
                        binding.fondoClima.setBackgroundResource(R.drawable.mist_background)
                        binding.tvDescripcionClima.setText("Neblina")
                    }
                }

                // De Kelvin
                binding.tvTempreratura.text = temp.toDouble().roundToInt().toString() + "°C"
                // fecha.text = "Fetch: $currentDate"
            },
            { binding.tvTempreratura.text = "Error" })

        queue.add(weatherRequest)

    }

    /**
     * It shows the weather forecast.
     */
    private fun mostrarResumenClima() {
        binding.btnPronosticoClima.setOnClickListener {
            val callIntent = Intent(this, ActivityMostrarResumenClima::class.java)
            startActivity(callIntent)
        }
    }

    private fun configurarObservable() {
        viewModel.listaNotificaciones.observe(this){ lista ->
            val arrPais = lista.toTypedArray()
            adaptador.arrNotificaciones = arrPais //Cambia la fuente de datos
            adaptador.notifyDataSetChanged() //Recarga todo
        }

    }

    override fun onStart() {
        super.onStart()
        viewModel.descargarDatosNoti()
    }

    private fun configurarRV() {
        val arrPaises = arrayOf(DataNotificacion(1, 2, 1, "20/10/2022", "08:39:03", "Sismo", "Se han activado las alertas sismicas", 19.5562.toFloat(), -99.2675.toFloat()),
                                DataNotificacion(2, 1, 2, "20/10/2022", "08:41:42", "Inundacion", "Periferico esta detenido debido a una Inundación, estamos trabajando en ello :)", 19.5562.toFloat(), -99.2675.toFloat()))
        val layout = LinearLayoutManager(this)
        adaptador = AdaptadorNotificaciones(this, arrPaises, object: AdaptadorNotificaciones.OnClickListener{
            override fun onItemClick(position: Int) {

                //println(adaptador.arrNotificaciones[0])
                val callIntent = Intent(this@MainActivity, DisplayMapsActivity::class.java)
                callIntent.putExtra(EXTRA_USER_MAP, adaptador.arrNotificaciones[position])
                startActivity(callIntent)

            }

        })
        binding.rvNotificaciones.adapter = adaptador
        binding.rvNotificaciones.layoutManager = layout

        //Separador
        val separador = DividerItemDecoration(this, layout.orientation)
        binding.rvNotificaciones.addItemDecoration(separador)
    }

    private fun mostrarAdvertencia() {
        val alerta = AlertDialog.Builder(this)
            .setTitle("Aviso")
            .setMessage("¿Seguro que quiere hacer la llamada de emergencia?")
            .setCancelable(false)
            .setPositiveButton("Aceptar") { _, _ ->
                var numberphone = "1911"
                val callIntenet = Intent(Intent.ACTION_CALL)
                callIntenet.data = Uri.parse("tel:$numberphone")
                startActivity(callIntenet)
            }
            .setNegativeButton("Cancelar"){_, _ -> }
        alerta.show() //lo hace visible
    }

    private fun checkPermissions() {

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), 101)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    fun verContactos(item: MenuItem): Boolean{
        val callIntenet = Intent(this, Contactos::class.java)
        startActivity(callIntenet)
        return true
    }
    fun verCreditos(item: MenuItem): Boolean{
        val callIntent = Intent(this, Creditos::class.java)
        startActivity(callIntent)
        return true
    }

    /*fun verMapa(item: MenuItem): Boolean{
        val callIntent = Intent(this, Mapa::class.java)
        startActivity(callIntent)
        return true
    }*/
}
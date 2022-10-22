package mx.itesm.dabt.tizalertap.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import mx.itesm.dabt.tizalertap.MainActivity
import mx.itesm.dabt.tizalertap.R
import mx.itesm.dabt.tizalertap.databinding.ActivityMostrarResumenClimaBinding
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class ActivityMostrarResumenClima : AppCompatActivity() {

    // View Binding
    private lateinit var binding: ActivityMostrarResumenClimaBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMostrarResumenClimaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // API Clima
        obtenerDatosAPI()

        // Boton Regresar
        btnRegresarMain()

        // Fecha y Hora
        obtenerFechaHora()
        supportActionBar?.title = "TizAlerta"
    }

    /**
     * It gets the current date and time.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun obtenerFechaHora() {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val fechaActual = LocalDateTime.now(ZoneId.of("America/Mexico_City")).format(formatter)
        binding.tvFechaHora.text = fechaActual.toString()
    }

    /**
     * It creates a button that when clicked, it will take you to the MainActivity.
     */
    private fun btnRegresarMain() {
        binding.btnRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * It makes a request to the OpenWeatherMap API and gets the weather data for Atizapán, Mexico.
     */
    private fun obtenerDatosAPI() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?q=Atizapán,MX&appid=c6747b86f2e57d515b3cdcdff6c68a35&units=metric&lang=es"

        val weatherRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Parte centrica
                val main = response.getJSONObject("main")
                val temp = main.getString("temp")
                val tempMin = main.getString("temp_min")
                val tempMax = main.getString("temp_max")

                //Parte de abajo
                val presion = main.getString("pressure")
                val humedad = main.getString("humidity")
                val wind = response.getJSONObject("wind")
                val viento = wind.getString("speed")

                // Descripcion de clima
                val clima = response.getJSONArray("weather")
                val primerIndice = clima.getJSONObject(0)
                val climaMain = primerIndice.getString("main")

                // Imagen de en medio
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

                var vientofinal = viento.toDouble() * 3.6

                if(vientofinal.roundToInt() <= 20){
                    binding.layoutViento.setBackgroundResource(R.drawable.clima_linear_layout_normal_background)
                } else if (vientofinal >= 21 && vientofinal <= 40) {
                    binding.layoutViento.setBackgroundResource(R.drawable.clima_linear_layout_moderado_background)
                } else if (vientofinal >= 41 && vientofinal <= 70) {
                    binding.layoutViento.setBackgroundResource(R.drawable.clima_linear_layout_fuerte_background)
                } else if (vientofinal >= 71 && vientofinal <= 120) {
                    binding.layoutViento.setBackgroundResource(R.drawable.clima_linear_layout_muy_fuerte_background)
                } else {
                    binding.layoutViento.setBackgroundResource(R.drawable.clima_linear_layout_huracanado_background)
                }

                binding.tvTemp.text = temp.toDouble().roundToInt().toString() + "°C"
                binding.tvTempMax.text = "Max Temp: " + tempMax.toDouble().roundToInt().toString() + "°C"
                binding.tvTempMin.text = "Min Temp: " + tempMin.toDouble().roundToInt().toString() + "°C"
                binding.tvHumedad.text = humedad + "%"
                binding.tvPresion.text = presion + "hPa"
                binding.tvViento.text = vientofinal.roundToInt().toString() + "Km/h"

            },
            { binding.tvTemp.text = "Error" })

        queue.add(weatherRequest)
    }
}
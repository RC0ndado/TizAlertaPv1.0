package mx.itesm.dabt.tizalertap.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mx.itesm.dabt.tizalertap.model.DataNotificacion
import mx.itesm.dabt.tizalertap.model.ServicioBD
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/* It's a ViewModel class that uses Retrofit to download a list of DataNotificacion objects from a PHP
script. */
class ListaNotiVM : ViewModel() {

    private val retrofit by lazy {
        // El objeto retrofit para instanciar el objeto que se conecta a la red y accede a los servicios definidos
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2/pruebaAndroidRetro/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val servicioNotificacionesAPI by lazy {
        retrofit.create(ServicioBD::class.java)
    }

    val listaNotificaciones = MutableLiveData<List<DataNotificacion>>()

    fun descargarDatosNoti() {
        val call = servicioNotificacionesAPI.descargarDatosNotificacion()
        println("Inicia descarga de datos")
        call.enqueue(object: Callback<List<DataNotificacion>> {
            override fun onResponse(call: Call<List<DataNotificacion>>, response: Response<List<DataNotificacion>>) {
                if (response.isSuccessful) {
                    println("Lista de notificaciones: ${response.body()}")
                    // Avisar a la vista (adaptador) que hay datos nuevos
                    listaNotificaciones.value = response.body()
                } else {
                    println("Error en los datos: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<DataNotificacion>>, t: Throwable) {
                println("Error en la descarga: ${t.localizedMessage}")
            }
        })
    }

}
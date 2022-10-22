package mx.itesm.dabt.tizalertap.model

import mx.itesm.dabt.tizalertap.model.DataNotificacion
import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

/* A class that is used to connect to the database and download the data. */
interface ServicioBD {

    @GET("db_getN.php")
    fun descargarDatosNotificacion(): Call<List<DataNotificacion>>
}

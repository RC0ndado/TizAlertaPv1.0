package mx.itesm.dabt.tizalertap.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * DataNotificacion is a data class, serializable, with 9 properties: id, id_suceso, id_entrada, fecha,
 * hora, titulo, descripcion, latitud, longitud
 * @property {Int} id - Int,
 * @property {Int} id_suceso - Int,
 * @property {Int} id_entrada - Int,
 * @property {String} fecha - String,
 * @property {String} hora - String,
 * @property {String} titulo - String,
 * @property {String} descripcion - String,
 * @property {Float} latitud - Float,
 * @property {Float} longitud - Float,
 */
data class DataNotificacion(
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_suceso")
    val id_suceso: Int,
    @SerializedName("id_entrada")
    val id_entrada: Int,
    @SerializedName("fecha")
    val fecha: String,
    @SerializedName("hora")
    val hora: String,
    @SerializedName("titulo")
    val titulo: String,
    @SerializedName("descripcion")
    val descripcion: String,
    @SerializedName("latitude")
    val latitud: Float,
    @SerializedName("longitude")
    val longitud: Float,
) : Serializable

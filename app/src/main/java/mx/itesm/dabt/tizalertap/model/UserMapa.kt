package mx.itesm.dabt.tizalertap.model

import java.io.Serializable

data class UserMapa(val title: String,
val places: List<DataNotificacion>) : Serializable

package mx.itesm.dabt.tizalertap.model

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import mx.itesm.dabt.tizalertap.MainActivity
import mx.itesm.dabt.tizalertap.R

class Notificaciones: FirebaseMessagingService() {


    /**
     * A function that is called when a notification is received.
     * 
     * @param token The new token.
     */
    private val channelName = "alertasPC" //canal
    private val channelId = "mx.itesm.dabt.tizalerta" //paquete

    override fun onNewToken(token: String){
        println("Token de este dispositivo: $token")
        //enviarTokenAPI(token) toke --> BD
    }

    /**
     * If the message received is a notification, then generate a notification.
     * 
     * @param message RemoteMessage - The RemoteMessage object containing the message data received
     * from Firebase Cloud Messaging.
     */
    override fun onMessageReceived(message: RemoteMessage) {
        println("LLega una NOTIFICACIÓN REMOTA")
        if (message.notification != null){
            generarNotificacion(message)
        }
    }

    /**
     * This function creates a notification with a custom layout, and it's called when a message is
     * received from Firebase Cloud Messaging.
     * 
     * @param message RemoteMessage
     */
    private fun generarNotificacion(message: RemoteMessage) {
        //Abre la app
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)  //si se abre una ventana que no sea la primera, hace que no haya historial

        val pendingIntent = PendingIntent.getActivity(this,
            0, intent, PendingIntent.FLAG_MUTABLE)

        var builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notisismoincendio)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
        builder = builder.setContent(crearVistaRemota(message))

        val admNotificaciones = getSystemService(FirebaseMessagingService.NOTIFICATION_SERVICE) as NotificationManager //adminsitra las notificaciones del sistema
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canalNotificaciones = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            admNotificaciones.createNotificationChannel(canalNotificaciones)
        }
        admNotificaciones.notify(0, builder.build())
    }

    //Genera la GUI para mostrar la notificacion
    @SuppressLint("RemoteViewLayout")
    private fun crearVistaRemota(message: RemoteMessage): RemoteViews {
        val titulo = message.notification?.title!!
        val mensaje = message.notification?.body!!
        val vistaRemota = RemoteViews("mx.itesm.dabt.tizalerta", R.layout.notificacion)
        vistaRemota.setTextViewText(R.id.tvTitulo, titulo)
        vistaRemota.setTextViewText(R.id.tvMensaje, mensaje)
        vistaRemota.setImageViewResource(R.id.imgIcono, R.drawable.megafono)
        return vistaRemota
    }


}
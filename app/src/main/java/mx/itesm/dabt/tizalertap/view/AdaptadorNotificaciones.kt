package mx.itesm.dabt.tizalertap.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import mx.itesm.dabt.tizalertap.model.DataNotificacion
import androidx.recyclerview.widget.RecyclerView
import mx.itesm.dabt.tizalertap.R

/* The class AdaptadorNotificaciones is a RecyclerView.Adapter that takes a Context, an Array of
DataNotificacion objects, and an OnClickListener object as parameters. */
class AdaptadorNotificaciones(val context: Context,
                             var arrNotificaciones: Array<DataNotificacion>,
                             val onClickListener: OnClickListener) :
    RecyclerView.Adapter<AdaptadorNotificaciones.RenglonNotificaciones>(){


    /* The interface OnClickListener has a method onItemClick which takes an integer as a parameter and
    returns nothing */
    interface OnClickListener{
        fun onItemClick(position: Int)
    }

    /**
     * The function onCreateViewHolder() is a function that returns a RenglonNotificaciones object. 
     * 
     * The function onCreateViewHolder() takes two parameters: parent and viewType. 
     * 
     * @param parent ViewGroup
     * @param viewType Int
     * @return A ViewHolder object.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RenglonNotificaciones {
        val vista = LayoutInflater.from(context)
            .inflate(R.layout.renglon_notificaciones, parent, false)
        return RenglonNotificaciones(vista)
    }

    /**
     * The onBindViewHolder function is called when the RecyclerView needs to display data at a
     * specified position. It binds the data for the item at the specified position and it also sets up
     * some private fields to be used by RecyclerView.
     * 
     * @param holder RenglonNotificaciones
     * @param position Int
     */
    override fun onBindViewHolder(holder: RenglonNotificaciones, position: Int) {
        val notificacion = arrNotificaciones[position]
        holder.itemView.setOnClickListener{
            onClickListener.onItemClick(position)
        }
        holder.set(notificacion)
    }

    /**
     * It returns the size of the array.
     * 
     * @return The size of the array.
     */
    override fun getItemCount(): Int {
        return arrNotificaciones.size
    }

    /* A class that is used to create a row in a RecyclerView. */
    class RenglonNotificaciones(var renglonNotificaciones: View): RecyclerView.ViewHolder(renglonNotificaciones) {
        /**
         * The function set() takes a DataNotificacion object as a parameter and sets the text of the
         * TextViews in the renglonNotificaciones layout to the values of the DataNotificacion object's
         * properties.
         * 
         * @param notificacion DataNotificacion
         */
        fun set(notificacion: DataNotificacion){
            val tvTituloN = renglonNotificaciones.findViewById<TextView>(R.id.tvTituloN)
            val tvDescripcion = renglonNotificaciones.findViewById<TextView>(R.id.tvDesc)
            val tvFecha = renglonNotificaciones.findViewById<TextView>(R.id.tvFecha)
            val tvHora = renglonNotificaciones.findViewById<TextView>(R.id.tvHora)
            tvTituloN.text = notificacion.titulo
            tvDescripcion.text = notificacion.descripcion
            tvFecha.text = "Fecha: " + notificacion.fecha
            tvHora.text = "Hora: " + notificacion.hora
        }

    }

}

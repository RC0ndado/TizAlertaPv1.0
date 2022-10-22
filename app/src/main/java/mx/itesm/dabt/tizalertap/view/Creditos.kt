package mx.itesm.dabt.tizalertap.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mx.itesm.dabt.tizalertap.R

/* It's a class that extends the AppCompatActivity class, and it's used to display the credits screen */
class Creditos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creditos)
        supportActionBar?.title = "TizAlerta"
    }
}
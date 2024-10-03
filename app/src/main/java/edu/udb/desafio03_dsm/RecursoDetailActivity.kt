package edu.udb.desafio03_dsm

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import edu.udb.desafio03_dsm.model.Recurso

class RecursoDetailActivity : AppCompatActivity() {

    private lateinit var recursoTitulo: TextView
    private lateinit var recursoDescripcion: TextView
    private lateinit var recursoImagen: ImageView
    private lateinit var recursoEnlace: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recurso_detail)

        recursoTitulo = findViewById(R.id.recursoTitulo)
        recursoDescripcion = findViewById(R.id.recursoDescripcion)
        recursoImagen = findViewById(R.id.recursoImagen)
        recursoEnlace = findViewById(R.id.recursoEnlace)
        // Obtener el recurso de los extras
        val recurso = intent.getParcelableExtra<Recurso>("recurso")

        recurso?.let {
            recursoTitulo.text = it.titulo
            recursoDescripcion.text = it.descripcion
            recursoEnlace.text = it.enlace
            Picasso.get().load(it.imagen).into(recursoImagen)
        }
    }
}

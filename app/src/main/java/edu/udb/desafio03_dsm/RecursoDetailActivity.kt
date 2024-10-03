package edu.udb.desafio03_dsm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import edu.udb.desafio03_dsm.model.Recurso
import edu.udb.desafio03_dsm.network.RetrofitInstance
import edu.udb.desafio03_dsm.ui.adapter.RecursoAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecursoDetailActivity : AppCompatActivity() {
    private lateinit var recursoAdapter: RecursoAdapter
    private lateinit var recursos: List<Recurso>
    private lateinit var recursoId: TextView
    private lateinit var recursoTitulo: TextView
    private lateinit var recursoDescripcion: TextView
    private lateinit var recursoImagen: ImageView
    private lateinit var recursoEnlace: TextView
    private lateinit var btnEliminar: Button
    private lateinit var btnModificar: Button
    private var recurso: Recurso? = null // Variable para almacenar el recurso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recurso_detail)

        // Inicializa tus vistas
        recursoId = findViewById(R.id.recursoId)
        recursoTitulo = findViewById(R.id.recursoTitulo)
        recursoDescripcion = findViewById(R.id.recursoDescripcion)
        recursoEnlace = findViewById(R.id.recursoEnlace)
        recursoImagen = findViewById(R.id.recursoImagen)
        btnEliminar = findViewById(R.id.btnEliminar)
        btnModificar = findViewById(R.id.btnModificar)
        // Obtener el recurso pasado desde el Intent
        recurso = intent.getParcelableExtra("recurso")

        recurso?.let {
            recursoId.text = it.id.toString() // Muestra el ID del recurso
            recursoTitulo.text = it.titulo
            recursoDescripcion.text = it.descripcion
            recursoEnlace.text = it.enlace


            // Cargar la imagen usando Picasso
            Picasso.get()
                .load(it.imagen)
                .placeholder(R.drawable.ic_launcher_foreground) // Imagen de carga
                .error(R.drawable.ic_launcher_foreground)
                .into(recursoImagen)
        }

        // Configurar el clic del botón para eliminar el recurso
        btnEliminar.setOnClickListener {
            recurso?.let {
                eliminarRecurso(it.id) // Llama a la función para eliminar el recurso
            }
        }

        btnModificar.setOnClickListener {
            recurso?.let {
                val intent = Intent(this, ModificarActivity::class.java)
                intent.putExtra("recurso", it) // Pasar el recurso a la nueva actividad
                startActivity(intent)
            }
        }
    }

    private fun eliminarRecurso(id: Int) {
        // Llamada a la API para eliminar el recurso
        val call = RetrofitInstance.api.deleteRecurso(id)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // El recurso se eliminó correctamente
                    Toast.makeText(this@RecursoDetailActivity, "Recurso eliminado con éxito", Toast.LENGTH_SHORT).show()
                    getRecursos()
                } else {
                    // Manejar el caso de error (ej. recurso no encontrado)
                    Toast.makeText(this@RecursoDetailActivity, "Error al eliminar recurso: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Manejar el error de conexión o cualquier otro error
                Toast.makeText(this@RecursoDetailActivity, "Error de conexión: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //en este caso luego de eliminar o modificar volvemos a llamar a todos los recursos "actualizados"
    private fun getRecursos() {
        RetrofitInstance.api.getRecursos().enqueue(object : Callback<List<Recurso>> {
            override fun onResponse(call: Call<List<Recurso>>, response: Response<List<Recurso>>) {
                if (response.isSuccessful) {
                    recursos = response.body() ?: emptyList() // Guardar los recursos en la variable
                    recursoAdapter.setRecursos(recursos) // Pasa los recursos al adaptador
                } else {
                    Log.e("MainActivity", "Error en la respuesta")
                }
            }

            override fun onFailure(call: Call<List<Recurso>>, t: Throwable) {
                Log.e("MainActivity", "Error en la llamada: ${t.message}")
            }
        })
    }
}

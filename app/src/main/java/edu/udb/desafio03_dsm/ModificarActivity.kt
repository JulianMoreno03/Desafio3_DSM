package edu.udb.desafio03_dsm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.udb.desafio03_dsm.model.Recurso
import edu.udb.desafio03_dsm.network.RetrofitInstance
import edu.udb.desafio03_dsm.ui.adapter.RecursoAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModificarActivity : AppCompatActivity() {
    private lateinit var editTextTitulo: EditText
    private lateinit var editTextDescripcion: EditText
    private lateinit var editTextEnlace: EditText
    private lateinit var editTextImagen: EditText
    private lateinit var btnGuardar: Button

    private lateinit var recursoAdapter: RecursoAdapter
    private lateinit var recursos: List<Recurso>
    private var recursoId: Int = 0 // Variable para almacenar el ID del recurso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar)

        // Inicializa las vistas
        editTextTitulo = findViewById(R.id.editTextTitulo)
        editTextDescripcion = findViewById(R.id.editTextDescripcion)
        editTextEnlace = findViewById(R.id.editTextEnlace)
        editTextImagen = findViewById(R.id.editTextImagen)
        btnGuardar = findViewById(R.id.btnGuardar)

        // Obtener el recurso pasado desde el Intent
        val recurso = intent.getParcelableExtra<Recurso>("recurso")
        recurso?.let {
            recursoId = it.id // Guarda el ID del recurso
            editTextTitulo.setText(it.titulo)
            editTextDescripcion.setText(it.descripcion)
            editTextEnlace.setText(it.enlace)
            editTextImagen.setText(it.imagen)
        }

        // Configurar el clic del botón para guardar los cambios
        btnGuardar.setOnClickListener {
            val nuevoRecurso = Recurso(
                id = recursoId,
                titulo = editTextTitulo.text.toString(),
                descripcion = editTextDescripcion.text.toString(),
                tipo = recurso?.tipo ?: "", // Mantener el tipo del recurso original
                enlace = editTextEnlace.text.toString(),
                imagen = editTextImagen.text.toString()
            )
            actualizarRecurso(nuevoRecurso)
        }
    }

    private fun actualizarRecurso(recurso: Recurso) {
        // Llamada a la API para actualizar el recurso
        val call = RetrofitInstance.api.updateRecurso(recurso.id, recurso)

        call.enqueue(object : Callback<Recurso> {
            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ModificarActivity, "Recurso actualizado con éxito", Toast.LENGTH_SHORT).show()
                    // Volver a MainActivity
                    val intent = Intent(this@ModificarActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK // Limpiar la pila de actividades
                    startActivity(intent)
                    finish() // Cerrar la actividad actual
                } else {
                    Toast.makeText(this@ModificarActivity, "Error al actualizar recurso: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                Toast.makeText(this@ModificarActivity, "Error de conexión: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

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

package edu.udb.desafio03_dsm

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.udb.desafio03_dsm.model.Recurso
import edu.udb.desafio03_dsm.network.RetrofitInstance
import edu.udb.desafio03_dsm.ui.adapter.RecursoAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddRecursoActivity : AppCompatActivity() {

    private lateinit var tituloEditText: EditText
    private lateinit var descripcionEditText: EditText
    private lateinit var tipoEditText: EditText
    private lateinit var enlaceEditText: EditText
    private lateinit var imagenEditText: EditText
    private lateinit var agregarRecursoButton: Button

    private lateinit var recursoAdapter: RecursoAdapter
    private lateinit var recursos: List<Recurso>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recurso)

        tituloEditText = findViewById(R.id.tituloEditText)
        descripcionEditText = findViewById(R.id.descripcionEditText)
        tipoEditText = findViewById(R.id.tipoEditText)
        enlaceEditText = findViewById(R.id.enlaceEditText)
        imagenEditText = findViewById(R.id.imagenEditText)
        agregarRecursoButton = findViewById(R.id.agregarRecursoButton)

        agregarRecursoButton.setOnClickListener {
            val titulo = tituloEditText.text.toString()
            val descripcion = descripcionEditText.text.toString()
            val tipo = tipoEditText.text.toString()
            val enlace = enlaceEditText.text.toString()
            val imagen = imagenEditText.text.toString()

            if (titulo.isNotEmpty() && descripcion.isNotEmpty() && tipo.isNotEmpty() && enlace.isNotEmpty() && imagen.isNotEmpty()) {
                val nuevoRecurso = Recurso(0, titulo, descripcion, tipo, enlace, imagen)
                addRecurso(nuevoRecurso)
            } else {
                Toast.makeText(this, "Por favor completa todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addRecurso(recurso: Recurso) {
        RetrofitInstance.api.addRecurso(recurso).enqueue(object : Callback<Recurso> {
            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddRecursoActivity, "Recurso agregado exitosamente", Toast.LENGTH_SHORT).show()
                    getRecursos()
                } else {
                    Toast.makeText(this@AddRecursoActivity, "Error al agregar el recurso", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                Toast.makeText(this@AddRecursoActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    //lo mismo, volvemos a llamar a todos los recursos para actualizar
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

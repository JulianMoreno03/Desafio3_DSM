package edu.udb.desafio03_dsm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import edu.udb.desafio03_dsm.model.Recurso
import edu.udb.desafio03_dsm.network.RetrofitInstance
import edu.udb.desafio03_dsm.ui.adapter.RecursoAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recursoAdapter: RecursoAdapter
    private lateinit var recursos: List<Recurso>
    private lateinit var searchView: SearchView
    private lateinit var btnNuevoRecurso: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        recursoAdapter = RecursoAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recursoAdapter
        btnNuevoRecurso = findViewById(R.id.btnNuevoRecurso)
        // Configurar la búsqueda
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    val id = it.toIntOrNull()
                    if (id != null) {
                        getRecursoById(id) // Llama al método para obtener el recurso por ID
                    } else {
                        Toast.makeText(this@MainActivity, "Por favor ingresa un ID válido.", Toast.LENGTH_SHORT).show()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Opcional: puedes implementar filtrado aquí si deseas.
                return false
            }
        })

        getRecursos() // Llama a la función para obtener todos los recursos


        // Configurar el evento para el botón de nuevo recurso
        btnNuevoRecurso.setOnClickListener {
            val intent = Intent(this, AddRecursoActivity::class.java)
            startActivity(intent)  // Inicia la nueva actividad
        }

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

    //OBTENER POR ID
    private fun getRecursoById(id: Int) {
        RetrofitInstance.api.getRecursoById(id).enqueue(object : Callback<Recurso> {
            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                if (response.isSuccessful) {
                    val recurso = response.body()
                    if (recurso != null) {
                        // Actualizar el adaptador con el recurso encontrado
                        recursoAdapter.setRecursos(listOf(recurso)) // Aquí mostramos solo el recurso encontrado
                    } else {
                        Log.e("MainActivity", "Recurso no encontrado")
                    }
                } else {
                    Log.e("MainActivity", "Error en la respuesta al buscar por ID: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                Log.e("MainActivity", "Error en la llamada: ${t.message}")
            }
        })
    }


}

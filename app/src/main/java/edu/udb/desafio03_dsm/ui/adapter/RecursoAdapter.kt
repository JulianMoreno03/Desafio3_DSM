package edu.udb.desafio03_dsm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.udb.desafio03_dsm.R
import edu.udb.desafio03_dsm.model.Recurso


class RecursoAdapter(private var recursos: List<Recurso> = emptyList()) : RecyclerView.Adapter<RecursoAdapter.RecursoViewHolder>() {
    private var recursosFiltrados: List<Recurso> = recursos // Lista filtrada

    class RecursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recursoTitulo: TextView = itemView.findViewById(R.id.recursoTitulo)
        val recursoImagen: ImageView = itemView.findViewById(R.id.recursoImagen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecursoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recurso, parent, false)
        return RecursoViewHolder(view)
    }
    override fun onBindViewHolder(holder: RecursoViewHolder, position: Int) {
        val recurso = recursos[position]
        holder.recursoTitulo.text = recurso.titulo

        // Cargar la imagen usando Picasso
        Picasso.get()
            .load(recurso.imagen)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground) // Imagen de error si no se puede cargar
            .into(holder.recursoImagen)
    }


    override fun getItemCount(): Int = recursos.size

    // Método para actualizar los recursos
    fun setRecursos(nuevosRecursos: List<Recurso>) {
        recursos = nuevosRecursos
        recursosFiltrados = nuevosRecursos // Actualiza la lista filtrada también
        notifyDataSetChanged()
    }

    // Método para filtrar los recursos
    fun filter(query: String) {
        recursosFiltrados = if (query.isEmpty()) {
            recursos // Si la búsqueda está vacía, mostrar todos los recursos
        } else {
            recursos.filter { recurso ->
                recurso.titulo.contains(query, ignoreCase = true) // Filtrar por título
            }
        }
        notifyDataSetChanged() // Notificar al adaptador que los datos han cambiado
    }
}


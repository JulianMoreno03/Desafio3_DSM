package edu.udb.desafio03_dsm.network

import edu.udb.desafio03_dsm.model.Recurso
import retrofit2.Call
import retrofit2.http.*

interface RecursoApiService {

    // Obtener todos los recursos
    @GET("recursos/recurso")
    fun getRecursos(): Call<List<Recurso>>

    // Obtener recurso por ID
    @GET("recursos/recurso/{id}")
    fun getRecursoById(@Path("id") id: Int): Call<Recurso>

    // Agregar un recurso
    @POST("recursos/recurso")
    fun addRecurso(@Body recurso: Recurso): Call<Recurso>

    // Modificar un recurso
    @PUT("recursos/recurso/{id}")
    fun updateRecurso(@Path("id") id: Int, @Body recurso: Recurso): Call<Recurso>

    // Eliminar un recurso
    @DELETE("recursos/recurso/{id}")
    fun deleteRecurso(@Path("id") id: Int): Call<Void>
}

package edu.udb.desafio03_dsm.model

//Modelo de datos que traera de la api
data class Recurso (
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val tipo: String,
    val enlace: String,
    val imagen: String
)
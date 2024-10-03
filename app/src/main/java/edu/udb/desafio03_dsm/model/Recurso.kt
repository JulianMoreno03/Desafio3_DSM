package edu.udb.desafio03_dsm.model

import android.os.Parcel
import android.os.Parcelable

// Modelo de datos que traerá de la API

data class Recurso(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val tipo: String,
    val enlace: String,
    val imagen: String
) : Parcelable {
    // Constructor que recibe un Parcel
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    // Método para escribir los datos en el Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(titulo)
        parcel.writeString(descripcion)
        parcel.writeString(tipo)
        parcel.writeString(enlace)
        parcel.writeString(imagen)
    }

    override fun describeContents(): Int {
        return 0
    }

    // Companion object para la creación de instancias de Parcelable
    companion object CREATOR : Parcelable.Creator<Recurso> {
        override fun createFromParcel(parcel: Parcel): Recurso {
            return Recurso(parcel)
        }

        override fun newArray(size: Int): Array<Recurso?> {
            return arrayOfNulls(size)
        }
    }
}

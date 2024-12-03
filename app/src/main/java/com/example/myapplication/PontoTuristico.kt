package com.example.myapplication

import android.os.Parcel
import android.os.Parcelable

data class PontoTuristico(
    val nome: String,
    val info1: String,
    val info2: String,
    val info3: String,
    val latitude: Double,  // Coordenada de latitude
    val longitude: Double  // Coordenada de longitude
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),  // Latitude
        parcel.readDouble()   // Longitude
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nome)
        parcel.writeString(info1)
        parcel.writeString(info2)
        parcel.writeString(info3)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<PontoTuristico> {
        override fun createFromParcel(parcel: Parcel): PontoTuristico {
            return PontoTuristico(parcel)
        }

        override fun newArray(size: Int): Array<PontoTuristico?> {
            return arrayOfNulls(size)
        }
    }
}

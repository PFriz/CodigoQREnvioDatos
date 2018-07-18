package cl.pfriz.applectorqr.Datos

import com.google.gson.annotations.SerializedName

/**
 * Clase creada automaticamente con el Json to kotlin class <3
 * mi amor eterno a esto
 * */
data class Empleado(
        @SerializedName("campo_rut") val campoRut: String,
        @SerializedName("0") val x0: String,
        @SerializedName("campo_nombre") val campoNombre: String,
        @SerializedName("1") val x1: String,
        @SerializedName("campo_apellido") val campoApellido: String,
        @SerializedName("2") val x2: String
)
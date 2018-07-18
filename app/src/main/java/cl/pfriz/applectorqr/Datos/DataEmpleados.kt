package cl.pfriz.applectorqr.Datos

import com.google.gson.annotations.SerializedName

/**
 * Clase creada automaticamente con el Json to kotlin class <3
 * mi amor eterno a esto
 * */
data class DataEmpleados(
        @SerializedName("estado") val estado: Int,
        @SerializedName("empleados") val empleados: List<Empleado>
)
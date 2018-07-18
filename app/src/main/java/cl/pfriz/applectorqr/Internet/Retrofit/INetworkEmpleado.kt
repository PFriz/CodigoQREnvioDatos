package cl.pfriz.applectorqr.Internet.Retrofit

import cl.pfriz.applectorqr.Datos.DataEmpleados
import io.reactivex.Observable
import retrofit2.http.GET

interface INetworkEmpleado{

    @GET("GetEmpleados.php")
    fun getAllEmpleados(): Observable<DataEmpleados>
}
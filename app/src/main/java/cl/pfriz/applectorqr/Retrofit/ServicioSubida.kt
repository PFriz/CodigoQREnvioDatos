package cl.pfriz.applectorqr.Retrofit

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

interface ServicioSubida {

    @GET("getempleados  .php")
    fun GetEmpleados(): Observable<Model.Result>

    companion object {
        fun create(): ServicioSubida {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("localhost/php/")
                    .build()

            return retrofit.create(ServicioSubida::class.java)
        }
    }


}